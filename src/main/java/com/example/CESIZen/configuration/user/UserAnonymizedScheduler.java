package com.example.CESIZen.configuration.user;

import com.example.CESIZen.model.user.UserAnonymizedLog;
import com.example.CESIZen.repository.UserAnonymizedLogRepository;
import com.example.CESIZen.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAnonymizedScheduler {

    private final UserRepository userRepository;
    private final UserAnonymizedLogRepository userAnonymizedLog;

    @Scheduled(cron = "0 0 2 * * SUN")
    @Transactional
    public void anonymizeInactiveUsers(){
        long updated = userRepository.anonymizeInactiveUsers();

        UserAnonymizedLog anonymizedLog = new UserAnonymizedLog(
                updated,
                LocalDateTime.now()
        );
        userAnonymizedLog.save(anonymizedLog);
        log.info("Anonymisation utilisateurs inactifs : {} comptes traités", updated);
    }

    @Scheduled(cron = "0 30 2 * * SUN")
    @Transactional
    public void cleanupOldAnonymizationLogs() {
        userAnonymizedLog.deleteByCreatedAtBefore();
        log.info("Nettoyage des logs d'anonymisation : {} entrées supprimées");
    }
}
