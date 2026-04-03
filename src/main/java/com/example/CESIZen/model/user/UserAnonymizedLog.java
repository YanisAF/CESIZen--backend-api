package com.example.CESIZen.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "anonymized_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnonymizedLog {
    @JsonProperty("user_id")
    @Id
    private Long userId;
    @JsonProperty("executed_at")
    private LocalDateTime executed_at;
}

