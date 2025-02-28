package com.stocks.Riskmanagement.entites;

import java.time.LocalDate;
import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RiskCalculator 
{
	@Id
	private String riskID;

	private Long orderID;
	private int accBalance;
	private int riskPerTrade;
	private double stopLoss;
	private int riskPoint;

	@Column(nullable = false)
	private double numOfShares;

	@Column(nullable = false, columnDefinition = "DEFAULT 'marketPlan'")
	private String typeOfPurchase;

	@Column(nullable = false, columnDefinition = "DEFAULT 'stopLoss'")
	private String typeOfSell;

	private double entryPrice;
	private LocalDate dateOfRiskCalc;

	@Column(nullable = false)
	private String transType;

	 @PrePersist
	 public void prePersist() 
	 {
	 	if (this.riskID == null || this.riskID.isEmpty())
	 	{
	 		this.riskID = generateRiskID();
	 	}
	 	validateConstraints();
	 }

	 @PreUpdate
	 public void preUpdate() 
	 {
	 	validateConstraints();
	 }

	private String generateRiskID() 
	{
		// Custom logic to generate riskID in the format RI0000
		return "RI" + String.format("%04d", new Random().nextInt(10000));
	}

	 private void validateConstraints() 
	 {
	 	if (!this.transType.equalsIgnoreCase("buy") && !this.transType.equalsIgnoreCase("sell")) 
	 	{
	 		throw new IllegalArgumentException("TransType must be either 'buy' or 'sell'.");
	 	}
	 	if (this.numOfShares < 0) 
	 	{
	 		throw new IllegalArgumentException("NumOfShares cannot be negative.");
	 	}
	 	if (!this.typeOfPurchase.equalsIgnoreCase("marketPlan") && !this.typeOfPurchase.equalsIgnoreCase("positionSizing")) 
	 	{
	 		throw new IllegalArgumentException("TypeOfPurchase must be either 'marketPlan' or 'positionSizing'.");
	 	}
	 	if (!this.typeOfSell.equalsIgnoreCase("marketPlan") && !this.typeOfSell.equalsIgnoreCase("stopLoss"))
	 	{
	 		throw new IllegalArgumentException("TypeOfSell must be either 'marketPlan' or 'stopLoss'.");
	 	}
	 }
}