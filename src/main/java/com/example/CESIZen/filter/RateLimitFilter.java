package com.example.CESIZen.filter;

import com.example.CESIZen.dto.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_WINDOW = 60;
    private static final long WINDOW_DURATION_MILLIS = 60_000L;
    // Durée d'inactivité après laquelle une IP est purgée du cache pour éviter les fuites mémoire
    private static final long ENTRY_TTL_MILLIS = 5 * 60_000L;

    private final Map<String, RequestCounter> counters = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "rate-limit-cleanup");
        thread.setDaemon(true);
        return thread;
    });

    public RateLimitFilter() {
        cleanupExecutor.scheduleAtFixedRate(this::evictStaleEntries, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = resolveClientIp(request);
        RequestCounter counter = counters.computeIfAbsent(clientIp, ip -> new RequestCounter());

        if (!counter.tryConsume()) {
            long retryAfterSeconds = counter.secondsUntilReset();

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
            response.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS_PER_WINDOW));
            response.setHeader("X-RateLimit-Remaining", "0");

            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.TOO_MANY_REQUESTS,
                    LocalDateTime.now(),
                    "Trop de requêtes envoyées. Merci de réessayer dans " + retryAfterSeconds + " secondes.",
                    request.getRequestURI()
            );

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            return;
        }

        response.setHeader("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS_PER_WINDOW));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(counter.remaining()));

        filterChain.doFilter(request, response);
    }

    /**
     * Récupère l'adresse IP réelle du client, en tenant compte d'un éventuel
     * reverse proxy (nginx) plaçant l'IP d'origine dans X-Forwarded-For.
     */
    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }

        return request.getRemoteAddr();
    }

    private void evictStaleEntries() {
        long now = System.currentTimeMillis();
        counters.entrySet().removeIf(entry -> (now - entry.getValue().windowStart.get()) > ENTRY_TTL_MILLIS);
    }

    @PreDestroy
    public void shutdown() {
        cleanupExecutor.shutdownNow();
    }

    /**
     * Compteur de requêtes associé à une IP pour la fenêtre de temps en cours.
     */
    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private final AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());

        synchronized boolean tryConsume() {
            resetIfWindowExpired();
            if (count.get() >= MAX_REQUESTS_PER_WINDOW) {
                return false;
            }
            count.incrementAndGet();
            return true;
        }

        synchronized int remaining() {
            return Math.max(0, MAX_REQUESTS_PER_WINDOW - count.get());
        }

        synchronized long secondsUntilReset() {
            long elapsed = System.currentTimeMillis() - windowStart.get();
            long remainingMillis = Math.max(0, WINDOW_DURATION_MILLIS - elapsed);
            return (remainingMillis + 999) / 1000;
        }

        private void resetIfWindowExpired() {
            long now = System.currentTimeMillis();
            if (now - windowStart.get() >= WINDOW_DURATION_MILLIS) {
                windowStart.set(now);
                count.set(0);
            }
        }
    }
}
