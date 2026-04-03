package com.example.CESIZen.service.user;

import com.example.CESIZen.dto.user.DeactivateResponseDto;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.model.user.User;
import com.example.CESIZen.model.user.UserAnonymizedLog;
import com.example.CESIZen.repository.UserAnonymizedLogRepository;
import com.example.CESIZen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDeactivateService {

    private final UserRepository userRepository;
    private final UserAnonymizedLogRepository userAnonymizedLogRepository;

    public DeactivateResponseDto deactivate(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (!user.isActive()) {
            throw new IllegalStateException("Ce compte est déjà désactivé");
        }

        user.setOriginalUsername(user.getUsername());
        user.setOriginalEmail(user.getEmail());

        String anonymizedSuffix = UUID.randomUUID().toString().substring(0, 8);
        user.setUsername("anonymized_" + anonymizedSuffix);
        user.setEmail("anonymized_" + anonymizedSuffix + "@deleted.local");
        user.setPhone(null);

        user.setActive(false);
        user.setAnonymizedAt(LocalDateTime.now());

        userRepository.save(user);

        UserAnonymizedLog log = new UserAnonymizedLog();
        log.setUserId(user.getId());
        log.setExecuted_at(LocalDateTime.now());

        userAnonymizedLogRepository.save(log);

        return new DeactivateResponseDto(user.getId(), "Compte désactivé avec succès", false);
    }

    public DeactivateResponseDto reactivate(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (user.isActive()) {
            throw new IllegalStateException("Ce compte est déjà actif");
        }

        user.setUsername(user.getOriginalUsername());
        user.setEmail(user.getOriginalEmail());
        user.setOriginalUsername(null);
        user.setOriginalEmail(null);

        user.setActive(true);
        user.setAnonymizedAt(null);

        userRepository.save(user);

        return new DeactivateResponseDto(user.getId(), "Compte réactivé avec succès", true);
    }
}
