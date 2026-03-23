package com.example.CESIZen.repository;

import com.example.CESIZen.model.user.UserAnonymizedLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnonymizedLogRepository extends JpaRepository<UserAnonymizedLog, Long> {
}
