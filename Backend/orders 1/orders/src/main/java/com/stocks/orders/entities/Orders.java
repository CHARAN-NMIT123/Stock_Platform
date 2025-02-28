package com.stocks.orders.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(nullable = false,insertable = false,updatable = false)
	private String accountId;

	@Column(nullable = false)
	private LocalDateTime dateOfOrder;

	@Column(nullable = false)
	private int numShares = 0;

	@Column(nullable = false)
	private int stockId;

	@Column(nullable = false, length = 50)
	
	private String symbol;

	@Column(nullable = false, length = 100)
	
	private String stockName;

	@Column(nullable = false)
	private String transType;

	@Column(nullable = false)
	private int balance;

	@Column(nullable = false)
	private int riskPerTrade;

	@Column(nullable = false)
	private int stopLoss;

	@Column(nullable = false)
	private String typeOfPurchase;

	@Column(nullable = false)
	private String typeOfSell;

	@Column(nullable = false)
	private double entryPrice;

	@Column(nullable = false, length = 50)
	private String transStatus;

	@Column(nullable = false)
	private boolean status;
	
	
	
	@ManyToOne
	@JoinColumn(name="accountId")
	private Account account;
	
	

	@PrePersist
	@PreUpdate
	private void validate() {

		// if (this.typeOfPurchase == null) {
		// 	this.typeOfPurchase = "marketPlan";
		// }
		// if (this.typeOfSell == null) {
		// 	this.typeOfSell = "stopLoss";
		// }
		if(dateOfOrder == null) {
			this.dateOfOrder = LocalDateTime.now();
		}
		if (numShares < 0) {
			throw new IllegalArgumentException("Number of shares cannot be negative");
		}
		if (!transType.equalsIgnoreCase("buy") && !transType.equalsIgnoreCase("sell")) {
			throw new IllegalArgumentException("Transaction type must be either 'buy' or 'sell'");
		}
		if (!typeOfPurchase.equalsIgnoreCase("marketPlan") && !typeOfPurchase.equalsIgnoreCase("positionSizing") && !typeOfPurchase.equalsIgnoreCase("stopLoss")) {
			throw new IllegalArgumentException("Type of purchase must be either 'marketPlan' or 'positionSizing'");
		}
		if (!typeOfSell.equalsIgnoreCase("marketPlan") && !typeOfSell.equalsIgnoreCase("stopLoss") && !typeOfSell.equalsIgnoreCase("positionSizing")) {
			throw new IllegalArgumentException("Type of sell must be either 'marketPlan' or 'stopLoss'");
		}
	}
}