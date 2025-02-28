
package com.stocks.stocksexchange.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long portfolioId;
    
    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private double balance = 0.0;

    private boolean status = true;

    @OneToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId", insertable = false, updatable = false)
    private Account account;

    @PreUpdate
    private void validateBalance() {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }
}
