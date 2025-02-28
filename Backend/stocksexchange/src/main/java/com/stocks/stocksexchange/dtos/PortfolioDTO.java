package com.stocks.stocksexchange.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {
	private Long portfolioId;
    private String accountId;
    private double balance;
    private boolean status;
}
