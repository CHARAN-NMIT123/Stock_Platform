package com.stocks.stocksexchange.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.stocks.stocksexchange.dtos.TradesDTO;
import com.stocks.stocksexchange.entities.Account;
import com.stocks.stocksexchange.entities.Stock;
import com.stocks.stocksexchange.entities.Trades;
import com.stocks.stocksexchange.exception.DatabaseException;
import com.stocks.stocksexchange.exception.InvalidInputException;
import com.stocks.stocksexchange.exception.ResourceNotFoundException;
import com.stocks.stocksexchange.exception.StockNotAvailableException;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.repositories.StockRepo;
import com.stocks.stocksexchange.repositories.TradesRepo;

@Service
public class TradesService {

	@Autowired
	private RestTemplate restTemplate;
	private final String buyUrlMarket = "http://localhost:9091/api/stock/buy/MarketPlan";
	private final String buyUrlPostionSizing = "http://localhost:9091/api/stock/buy/PositionSizing";
	private final String sellUrlStopLoss = "http://localhost:9091/api/stock/sell/stopLoss";
	private final String sellUrlMarket = "http://localhost:9091/api/stock/sell/MarketPlan";
	@Autowired
	private TradesRepo tradesRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private StockRepo stockRepo;

	@Autowired
	private AccountRepo accountRepo;

	public List<TradesDTO> getAllTrades() {
		return tradesRepo.findAll().stream().map(trade -> modelMapper.map(trade, TradesDTO.class))
				.collect(Collectors.toList());
	}

	public ResponseEntity<Void> processTrade(TradesDTO tradesDTO, String token) {
		Account account = accountRepo.findById(tradesDTO.getAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		Stock stock = stockRepo.findById(tradesDTO.getStockId())
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		if (!stock.isStatus()) {
			throw new StockNotAvailableException("Does not exist anymore");
		}
		Trades trades = modelMapper.map(tradesDTO, Trades.class);
		trades.setAccount(account);
		trades.setStock(stock);
		TradesDTO updatedtradesDTO = modelMapper.map(trades, TradesDTO.class);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<TradesDTO> requestEntity = new HttpEntity<>(updatedtradesDTO, headers);

		if (trades.getTransType().equalsIgnoreCase("Buy")) {
			if (trades.getTypeOfPurchase().equalsIgnoreCase("MarketPlan")) {
				ResponseEntity<Void> buyResponse = restTemplate.postForEntity(buyUrlMarket, requestEntity, Void.class);
				return new ResponseEntity<>(buyResponse.getStatusCode());
			} else if (trades.getTypeOfPurchase().equalsIgnoreCase("PositionSizing")) {
				ResponseEntity<Void> buyResponse = restTemplate.postForEntity(buyUrlPostionSizing, requestEntity,
						Void.class);
				return new ResponseEntity<>(buyResponse.getStatusCode());
			} else {
				throw new InvalidInputException("Invalid transaction type");
			}
		} else if (tradesDTO.getTransType().equalsIgnoreCase("Sell")) {
			if (trades.getTypeOfSell().equalsIgnoreCase("MarketPlan")) {
				ResponseEntity<Void> sellResponse = restTemplate.postForEntity(sellUrlMarket, requestEntity,
						Void.class);
				return new ResponseEntity<>(sellResponse.getStatusCode());
			} else if (trades.getTypeOfSell().equalsIgnoreCase("StopLoss")) {
				ResponseEntity<Void> sellResponse = restTemplate.postForEntity(sellUrlStopLoss, requestEntity,
						Void.class);
				return new ResponseEntity<>(sellResponse.getStatusCode());
			} else {
				throw new InvalidInputException("Invalid transaction type");
			}
		} else {
			throw new InvalidInputException("Invalid transaction type");
		}
	}

	public TradesDTO updateTrade(TradesDTO tradesDTO) {
		Trades trade = modelMapper.map(tradesDTO, Trades.class);
		Trades updatedTrade = tradesRepo.save(trade);
		return modelMapper.map(updatedTrade, TradesDTO.class);
	}

	public int processMarketSellOrder(TradesDTO tradeDTO) {
		// Create the trade record
		Trades trade = modelMapper.map(tradeDTO, Trades.class);
		trade.setTransType("SELL");
		trade.setTypeOfSell("MARKET");
		Trades savedTrade = tradesRepo.save(trade);

		// Update stock total shares
		Stock stock = stockRepo.findById(trade.getStockId())
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		stock.setTotalShares(stock.getTotalShares() + trade.getNumShares());
		stockRepo.save(stock);

		return savedTrade.getNumShares();
	}

	public int processMarketBuyOrder(TradesDTO tradeDTO) {
		// Create the trade record

		Trades trade = modelMapper.map(tradeDTO, Trades.class);
		trade.setTransType("BUY");
		trade.setTypeOfPurchase("MARKET");

		// Verify sufficient shares available
		Stock stock = stockRepo.findById(trade.getStockId())
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		if (stock.getTotalShares() < trade.getNumShares()) {
			throw new DatabaseException("Insufficient shares available");
		}

		// Update stock total shares
		stock.setTotalShares(stock.getTotalShares() - trade.getNumShares());
		stockRepo.save(stock);

		Trades savedTrade = tradesRepo.save(trade);
		return savedTrade.getNumShares();
	}

	public int processPositionBuyOrder(TradesDTO tradeDTO) {
		Trades trade = modelMapper.map(tradeDTO, Trades.class);
		Stock stock = stockRepo.findById(trade.getStockId())
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		stock.setTotalShares(stock.getTotalShares() - trade.getNumShares());
		stockRepo.save(stock);

		Trades savedTrade = tradesRepo.save(trade);
		return savedTrade.getNumShares();

	}

	public int processStopSellOrder(TradesDTO tradeDTO) {
		Trades trade = modelMapper.map(tradeDTO, Trades.class);
		Stock stock = stockRepo.findById(trade.getStockId())
				.orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		stock.setTotalShares(stock.getTotalShares() + trade.getNumShares());
		stockRepo.save(stock);

		Trades savedTrade = tradesRepo.save(trade);
		return savedTrade.getNumShares();
	}

	public List<TradesDTO> getAllTradesByAccId(String accountId) {
		return tradesRepo.findByAccountId(accountId).stream().map(Trades -> modelMapper.map(Trades, TradesDTO.class))
				.collect(Collectors.toList());
	}

	public List<TradesDTO> getTradesFromLast5Days(String accountId) {
		LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(6);
		return tradesRepo.findByAccountIdAndDateOfOrderAfter(accountId, fiveDaysAgo).stream()
				.map(Trades -> modelMapper.map(Trades, TradesDTO.class)).collect(Collectors.toList());
	}

}
