package com.example.CESIZen.repository;

import com.example.CESIZen.model.user.UserAnonymizedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserAnonymizedLogRepository extends JpaRepository<UserAnonymizedLog, Long> {

    @Query(nativeQuery = true, value = """
            DELETE
            FROM anonymized_log
            WHERE executed_at < NOW() - INTERVAL '36 months 1 week'""")
    void deleteByCreatedAtBefore();
}
