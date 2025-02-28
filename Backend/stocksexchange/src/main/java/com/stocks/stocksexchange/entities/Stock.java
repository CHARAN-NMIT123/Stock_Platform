package com.stocks.stocksexchange.entities;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int stockId;

    private String name;

    @Lob
    @Size(max = 4)
    private String symbol;

    @Min(0)
    private int totalShares;

    @Min(0)
    private double open;

    @Min(0)
    private double last;

    private boolean status;

//    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Trades> trades;

    private String type;
    
//    @PrePersist
//    public void generateId() {
//        this.stockId = counter++;
//    }
}
