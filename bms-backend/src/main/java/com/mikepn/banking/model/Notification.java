package com.mikepn.banking.model;


import com.mikepn.banking.enums.notification.ENotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_notification")
public class Notification extends Base {

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private ENotification notificationType;

    private boolean isRead;
    private LocalDateTime createdAt;

    @ManyToOne
    private Account account;
}
