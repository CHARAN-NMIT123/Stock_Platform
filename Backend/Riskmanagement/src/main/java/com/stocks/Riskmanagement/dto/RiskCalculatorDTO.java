package com.stocks.Riskmanagement.dto;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskCalculatorDTO 
{    
	@Id
	private String riskID;

	private long orderID;
	private int accBalance;
	private int riskPerTrade;
	private int stopLoss;
	private int riskPoint;
	private int numOfShares;
	private String typeOfPurchase;
	private String typeOfSell;
	private double entryPrice;
	private LocalDate dateOfRiskCalc;
	private String transType;

}
