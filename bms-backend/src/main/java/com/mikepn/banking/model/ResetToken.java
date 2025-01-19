package com.mikepn.banking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "reset_token")
public class ResetToken extends Base {

    private static final int EXPIRATION = 60 * 24;


    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;


}
