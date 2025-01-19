package com.mikepn.banking.model;

import com.mikepn.banking.enums.account.EAccountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_account")
public class Account extends Base {

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private EAccountType accountType;

    private double balance = 0.0;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "account")
    private List<Transaction> transactions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    private List<Loan> loans;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    private List<Notification> notifications;

}
