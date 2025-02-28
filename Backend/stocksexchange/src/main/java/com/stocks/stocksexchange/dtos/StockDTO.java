package com.stocks.stocksexchange.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
    private int stockId;
    private String name;
    private String symbol;
    private int totalShares;
    private double open;
    private double last;
    private boolean status;
    private String type;
}
