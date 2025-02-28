package com.stocks.stocksexchange.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradesDTO {
    private Long tradeId;
    private String accountId;
    private LocalDateTime dateOfOrder;
    private int numShares;
    private int stockId;
    private String symbol;
    private String transType;
    private String typeOfPurchase;
    private String typeOfSell;
    private double tradedAt;
    private boolean status;
}
