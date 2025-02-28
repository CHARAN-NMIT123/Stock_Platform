	package com.stocks.stocksexchange.entities;
	
	import java.time.LocalDateTime;
	
	import jakarta.persistence.CascadeType;
	import jakarta.persistence.Column;
	import jakarta.persistence.Entity;
	import jakarta.persistence.Id;
	import jakarta.persistence.JoinColumn;
	import jakarta.persistence.ManyToOne;
	import jakarta.persistence.PrePersist;
	import jakarta.persistence.PreUpdate;
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	
	@Entity
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class Trades {
	    @Id
	    private Long tradeId;
	
	    private String accountId;
	
	    @Column(nullable = false)
	    private LocalDateTime dateOfOrder;
	
	    @Column(nullable = false)
	    private int numShares;
	
	    @Column(nullable = false)
	    private int stockId;
	
	    @Column(nullable = false, length = 50)
	    private String symbol;
	
	    @Column(nullable = false)
	    private String transType;
	
	    @Column(nullable = false)
	    private String typeOfPurchase;
	
	    @Column(nullable = false)
	    private String typeOfSell;
	
	    @Column(nullable = false)
	    private double tradedAt;
	
	    @Column(nullable = false)
	    private boolean status;
	
	    @ManyToOne
	    @JoinColumn(name = "accountId", insertable = false, updatable = false, nullable = false)
	    private Account account;
	
	    @ManyToOne
	    @JoinColumn(name = "stockId", insertable = false, updatable = false, nullable = false)
	    private Stock stock;
	
	    @PrePersist
	    @PreUpdate
	    private void validateTradeAndSyncTrade() {
	        if (numShares < 0) {
	            throw new IllegalArgumentException("Number of shares cannot be negative");
	        }
	        if (tradedAt < 0) {
	            throw new IllegalArgumentException("Price cannot be negative");
	        }
	        if (dateOfOrder == null) {
	            dateOfOrder = LocalDateTime.now();
	        }
	        if (dateOfOrder.isAfter(LocalDateTime.now())) {
	            throw new IllegalArgumentException("Date and time cannot be in the future");
	        }
	        if (transType != null && !transType.equalsIgnoreCase("BUY") && !transType.equalsIgnoreCase("SELL")) {
	            throw new IllegalArgumentException("Transaction type must be either BUY or SELL");
	        }
	    }
	}
