package com.stocks.stocksexchange.serviceTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.stocks.stocksexchange.dtos.TradesDTO;
import com.stocks.stocksexchange.entities.Account;
import com.stocks.stocksexchange.entities.Stock;
import com.stocks.stocksexchange.entities.Trades;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.repositories.StockRepo;
import com.stocks.stocksexchange.repositories.TradesRepo;
import com.stocks.stocksexchange.services.TradesService;

@ExtendWith(MockitoExtension.class)
public class TradesServiceTest {

	@Mock
	private TradesRepo tradesRepo;

	@Mock
	private StockRepo stockRepo;

	@Mock
	private AccountRepo accountRepo;

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private TradesService tradesService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetAllTrades() {
		Trades trade1 = new Trades(1L, "A1", LocalDateTime.now(), 100, 1, "STKA", "BUY", "MarketPlan", null, 10.0, true,
				null, null);
		Trades trade2 = new Trades(2L, "A2", LocalDateTime.now(), 200, 2, "STKB", "SELL", "MarketPlan", null, 20.0,
				true, null, null);
		List<Trades> trades = Arrays.asList(trade1, trade2);

		TradesDTO tradesDTO1 = new TradesDTO(1L, "A1", LocalDateTime.now(), 100, 1, "STKA", "BUY", "MarketPlan", null,
				10.0, true);
		TradesDTO tradesDTO2 = new TradesDTO(2L, "A2", LocalDateTime.now(), 200, 2, "STKB", "SELL", "MarketPlan", null,
				20.0, true);

		when(tradesRepo.findAll()).thenReturn(trades);
		when(modelMapper.map(trade1, TradesDTO.class)).thenReturn(tradesDTO1);
		when(modelMapper.map(trade2, TradesDTO.class)).thenReturn(tradesDTO2);

		List<TradesDTO> tradesDTOs = tradesService.getAllTrades();

		Assertions.assertEquals(2, tradesDTOs.size());
		Assertions.assertTrue(tradesDTOs.contains(tradesDTO1));
		Assertions.assertTrue(tradesDTOs.contains(tradesDTO2));
	}

	@Test
	void testProcessTrade() {
		TradesDTO tradesDTO = new TradesDTO(1L, "A1", LocalDateTime.now(), 100, 1, "STKA", "BUY", "MarketPlan", null,
				10.0, true);
		Account account = new Account();
		account.setAccountId("A1");
		Stock stock = new Stock(1, "Stock A", "STKA", 1000, 10.0, 12.0, true, null);
		Trades trade = new Trades(1L, "A1", LocalDateTime.now(), 100, 1, "STKA", "BUY", "MarketPlan", null, 10.0, true,
				account, stock);

		when(accountRepo.findById("A1")).thenReturn(Optional.of(account));
		when(stockRepo.findById(1)).thenReturn(Optional.of(stock));
		when(modelMapper.map(tradesDTO, Trades.class)).thenReturn(trade);
		when(modelMapper.map(trade, TradesDTO.class)).thenReturn(tradesDTO);

		// Mock RestTemplate's postForEntity method to avoid actual HTTP call
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", null);
		HttpEntity<TradesDTO> requestEntity = new HttpEntity<>(tradesDTO, headers);
		when(restTemplate.postForEntity("http://localhost:9091/api/stock/buy/MarketPlan", requestEntity, Void.class))
				.thenReturn(ResponseEntity.ok().build());

		ResponseEntity<Void> response = tradesService.processTrade(tradesDTO, null);

		Assertions.assertNotNull(response);
//        Assertions.assertEquals(200, response.getStatusCodeValue());
		verify(accountRepo, times(1)).findById("A1");
		verify(stockRepo, times(1)).findById(1);
	}

	@Test
	void testUpdateTrade() {
		TradesDTO tradesDTO = new TradesDTO(2L, "A2", LocalDateTime.now(), 200, 2, "STKB", "SELL", "MarketPlan", null,
				20.0, true);
		Trades trade = new Trades(2L, "A2", LocalDateTime.now(), 200, 2, "STKB", "SELL", "MarketPlan", null, 20.0, true,
				null, null);
		Trades updatedTrade = new Trades(2L, "A2", LocalDateTime.now(), 200, 2, "STKB", "SELL", "MarketPlan", null,
				20.0, true, null, null);

		when(modelMapper.map(tradesDTO, Trades.class)).thenReturn(trade);
		when(tradesRepo.save(trade)).thenReturn(updatedTrade);
		when(modelMapper.map(updatedTrade, TradesDTO.class)).thenReturn(tradesDTO);

		TradesDTO result = tradesService.updateTrade(tradesDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(tradesDTO, result);
		verify(tradesRepo, times(1)).save(trade);
	}

	@Test
	void testProcessMarketSellOrder() {
		TradesDTO tradeDTO = new TradesDTO(3L, "A3", LocalDateTime.now(), 150, 3, "STKC", "SELL", "MARKET", null, 15.0,
				true);
		Trades trade = new Trades(3L, "A3", LocalDateTime.now(), 150, 3, "STKC", "SELL", "MARKET", null, 15.0, true,
				null, null);
		Stock stock = new Stock(3, "Stock C", "STKC", 2000, 15.0, 17.0, true, null);

		when(modelMapper.map(tradeDTO, Trades.class)).thenReturn(trade);
		when(stockRepo.findById(3)).thenReturn(Optional.of(stock));
		when(tradesRepo.save(trade)).thenReturn(trade);

		int result = tradesService.processMarketSellOrder(tradeDTO);

		Assertions.assertEquals(150, result);
		verify(stockRepo, times(1)).findById(3);
		verify(stockRepo, times(1)).save(stock);
		verify(tradesRepo, times(1)).save(trade);
	}

	@Test
	void testProcessMarketBuyOrder() {
		TradesDTO tradeDTO = new TradesDTO(4L, "A4", LocalDateTime.now(), 200, 4, "STKD", "BUY", "MARKET", null, 20.0,
				true);
		Trades trade = new Trades(4L, "A4", LocalDateTime.now(), 200, 4, "STKD", "BUY", "MARKET", null, 20.0, true,
				null, null);
		Stock stock = new Stock(4, "Stock D", "STKD", 2500, 25.0, 28.0, true, null);

		when(modelMapper.map(tradeDTO, Trades.class)).thenReturn(trade);
		when(stockRepo.findById(4)).thenReturn(Optional.of(stock));
		when(stockRepo.save(stock)).thenReturn(stock);
		when(tradesRepo.save(trade)).thenReturn(trade);

		int result = tradesService.processMarketBuyOrder(tradeDTO);

		Assertions.assertEquals(200, result);
		verify(stockRepo, times(1)).findById(4);
		verify(stockRepo, times(1)).save(stock);
		verify(tradesRepo, times(1)).save(trade);
	}



@Test
void testGetTradesFromLast5Days() {
    String accountId = "A1";
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime fiveDaysAgo = now.minusDays(6);

    Trades trade1 = new Trades(1L, accountId, now.minusDays(2), 100, 1, "STKA", "BUY", "MarketPlan", null, 10.0, true, null, null);
    Trades trade2 = new Trades(2L, accountId, now.minusDays(3), 200, 2, "STKB", "SELL", "MarketPlan", null, 20.0, true, null, null);
    List<Trades> trades = Arrays.asList(trade1, trade2);

    TradesDTO tradesDTO1 = new TradesDTO(1L, accountId, now.minusDays(2), 100, 1, "STKA", "BUY", "MarketPlan", null, 10.0, true);
    TradesDTO tradesDTO2 = new TradesDTO(2L, accountId, now.minusDays(3), 200, 2, "STKB", "SELL", "MarketPlan", null, 20.0, true);

    when(tradesRepo.findByAccountIdAndDateOfOrderAfter(eq(accountId), any(LocalDateTime.class))).thenReturn(trades);

    when(modelMapper.map(trade1, TradesDTO.class)).thenReturn(tradesDTO1);
    when(modelMapper.map(trade2, TradesDTO.class)).thenReturn(tradesDTO2);

    List<TradesDTO> tradesDTOs = tradesService.getTradesFromLast5Days(accountId);

    Assertions.assertTrue(tradesDTOs.contains(tradesDTO1));
    Assertions.assertTrue(tradesDTOs.contains(tradesDTO2));
    
    ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
    verify(tradesRepo).findByAccountIdAndDateOfOrderAfter(eq(accountId), dateCaptor.capture());
    Assertions.assertEquals(fiveDaysAgo.toLocalDate(), dateCaptor.getValue().toLocalDate());
}


	@Test
	void testProcessPositionBuyOrder() {
		TradesDTO tradeDTO = new TradesDTO(5L, "A5", LocalDateTime.now(), 250, 5, "STKE", "BUY", "PositionSizing", null,
				25.0, true);
		Trades trade = new Trades(5L, "A5", LocalDateTime.now(), 250, 5, "STKE", "BUY", "PositionSizing", null, 25.0,
				true, null, null);
		Stock stock = new Stock(5, "Stock E", "STKE", 3000, 30.0, 35.0, true, null);

		when(modelMapper.map(tradeDTO, Trades.class)).thenReturn(trade);
		when(stockRepo.findById(5)).thenReturn(Optional.of(stock));
		when(tradesRepo.save(trade)).thenReturn(trade);

		int result = tradesService.processPositionBuyOrder(tradeDTO);

		Assertions.assertEquals(250, result);
		verify(stockRepo, times(1)).findById(5);
		verify(stockRepo, times(1)).save(stock);
		verify(tradesRepo, times(1)).save(trade);
	}

	@Test
	void testProcessStopSellOrder() {
		TradesDTO tradeDTO = new TradesDTO(6L, "A6", LocalDateTime.now(), 300, 6, "STKF", "SELL", null, "StopLoss",
				30.0, true);
		Trades trade = new Trades(6L, "A6", LocalDateTime.now(), 300, 6, "STKF", "SELL", null, "StopLoss", 30.0, true,
				null, null);
		Stock stock = new Stock(6, "Stock F", "STKF", 4000, 40.0, 45.0, true, null);

		when(modelMapper.map(tradeDTO, Trades.class)).thenReturn(trade);
		when(stockRepo.findById(6)).thenReturn(Optional.of(stock));
		when(tradesRepo.save(trade)).thenReturn(trade);

		int result = tradesService.processStopSellOrder(tradeDTO);

		Assertions.assertEquals(300, result);
		verify(stockRepo, times(1)).findById(6);
		verify(stockRepo, times(1)).save(stock);
		verify(tradesRepo, times(1)).save(trade);
	}
}