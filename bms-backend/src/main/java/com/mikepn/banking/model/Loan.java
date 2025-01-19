package com.mikepn.banking.model;


import com.mikepn.banking.enums.loan.ELoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_loan")
public class Loan extends Base{

    @Enumerated(EnumType.STRING)
    private ELoanType loanType;

    private double loanAmount;
    private double interestRate;
    private Integer loanPeriod;

    @ManyToOne
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;

}
