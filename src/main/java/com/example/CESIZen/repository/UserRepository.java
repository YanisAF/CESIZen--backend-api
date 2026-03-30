package com.example.CESIZen.repository;

import com.example.CESIZen.model.user.User;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    Optional<User> getByEmail(String email);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.phone = :identifier")
    Optional<User> findByEmailOrPhone(@Param("identifier") String identifier);

    @Query("""
            SELECT u
                FROM User u
                WHERE u.role = 'ROLE_USER'
            """)
    List<User> findAllUserIfUserRole();

    @Query(value = """
            UPDATE users
            SET
               email = CONCAT('anon_', id, '@example.com')
               user_name = 'ANONYMISÉ',
               anonymized_at = NOW()
            WHERE last_activity_at < NOW() - INTERVAL '36 months'
            AND anonymized_at IS NULL
            """, nativeQuery = true)
    int anonymizeInactiveUsers();

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = """
            DELETE FROM password_reset_token
            WHERE user_id = :userId""")
    void deleteResetTokenByUserId(Long userId);
}
