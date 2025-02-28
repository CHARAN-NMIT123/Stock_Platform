package com.stocks.Riskmanagement.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO 
{
    private Long orderId;
    private String accountId;
    private LocalDateTime dateOfOrder;
    private int numShares;
    private int stockId;
    private String symbol;
    private String stockName;   
    private String transType;
    private int balance;
    private int riskPerTrade;
    private int stopLoss;
    private String typeOfPurchase;
    private String typeOfSell;
    private double entryPrice;
    private String transStatus;
    private boolean status;

}