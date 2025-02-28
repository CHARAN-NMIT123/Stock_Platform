package com.stocks.stocksexchange.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.stocksexchange.dtos.OrderDTO;
import com.stocks.stocksexchange.dtos.TradesDTO;
import com.stocks.stocksexchange.services.TradesService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/stock")
public class TradesController {

	@Autowired
	private TradesService tradesService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/getOders")
	@PreAuthorize("hasAuthority('TRADER') || hasAuthority('RISKMANAGEMENT')")
	public ResponseEntity<String> getOrder(@RequestBody OrderDTO orderDTO, Authentication authentication,
			HttpServletRequest httpServletRequest) {
		TradesDTO tradesDTO = modelMapper.map(orderDTO, TradesDTO.class);
		String token = httpServletRequest.getHeader("Authorization");
		tradesService.processTrade(tradesDTO, token);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/sell/MarketPlan")
	@PreAuthorize("hasAuthority('TRADER')")
	public ResponseEntity<Integer> processMarketSellOrder(@RequestBody TradesDTO tradeDTO) {
		// TradesDTO tradeDTO = modelMapper.map(orderDTO, TradesDTO.class);
		int unitsSold = tradesService.processMarketSellOrder(tradeDTO);
		return new ResponseEntity<>(unitsSold, HttpStatus.OK);
	}

	@PostMapping("/buy/MarketPlan")
	@PreAuthorize("hasAuthority('TRADER')")
	public ResponseEntity<Integer> processMarketBuyOrder(@RequestBody TradesDTO tradeDTO) {
		// TradesDTO tradeDTO = modelMapper.map(orderDTO, TradesDTO.class);
		int unitsBought = tradesService.processMarketBuyOrder(tradeDTO);
		return new ResponseEntity<>(unitsBought, HttpStatus.OK);
	}

	@PostMapping("/buy/PositionSizing")
	@PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
	public ResponseEntity<Integer> processPositionBuyOrder(@RequestBody TradesDTO tradeDTO) {
		int unitsBought = tradesService.processPositionBuyOrder(tradeDTO);
		return new ResponseEntity<>(unitsBought, HttpStatus.OK);
	}

	@PostMapping("/sell/stopLoss")
	@PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
	public ResponseEntity<Integer> processstopLossSellOrder(@RequestBody TradesDTO tradeDTO) {
		// TradesDTO tradeDTO = modelMapper.map(orderDTO, TradesDTO.class);
		int unitsBought = tradesService.processStopSellOrder(tradeDTO);
		return new ResponseEntity<>(unitsBought, HttpStatus.OK);
	}

//	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasAuthority('STOCKADMIN') || hasAuthority('TRADER')")
	@GetMapping("/{accountId}")
	public ResponseEntity<List<TradesDTO>> getAccountPortfolios(@PathVariable String accountId,
			Authentication authentication) {
		List<TradesDTO> tradesDTO = tradesService.getAllTradesByAccId(accountId);
		return ResponseEntity.ok(tradesDTO);
	}

//	@SecurityRequirement(name = "Bearer Authentication")
	@PreAuthorize("hasAuthority('STOCKADMIN') || hasAuthority('TRADER')")
	@GetMapping("/trades/last5days/{accountId}")
	public List<TradesDTO> getTradesFromLast5Days(@PathVariable String accountId, Authentication authentication) {
		return tradesService.getTradesFromLast5Days(accountId);
	}

}
