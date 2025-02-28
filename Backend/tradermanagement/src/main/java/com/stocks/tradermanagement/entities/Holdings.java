package com.stocks.tradermanagement.entities;

//import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holdings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int holdingId;
    @Column(insertable = false, updatable = false)
    private String accountId;
    private int stockId;

    @Column(nullable = false)
    @Positive
    private double boughtAt;

    private double soldAt;

    @Column(nullable = false)
    @Positive
    private int numShares;
    private String status;    
    
    @ManyToOne
    @JoinColumn(name="accountId")
    private Account account;
}
