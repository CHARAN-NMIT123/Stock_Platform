package com.stocks.tradermanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoldingDTO {
    private int holdingId;
    private String accountId;
    private int stockId;
    private double boughtAt;
    private double soldAt;
    private int numShares;
    private String status;    
}
