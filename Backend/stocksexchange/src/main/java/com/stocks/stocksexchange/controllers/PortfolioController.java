package com.stocks.stocksexchange.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.stocksexchange.dtos.BalanceUpdateDTO;
import com.stocks.stocksexchange.dtos.PortfolioDTO;
import com.stocks.stocksexchange.entities.Portfolio;
import com.stocks.stocksexchange.services.PortfolioService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PortfolioController {

	@Autowired
	private PortfolioService portfolioService;

	@Autowired
	private ModelMapper modelMapper;

//	@GetMapping("/{accountId}")
//	@PreAuthorize("hasAuthority('STOCKADMIN')")
//	public ResponseEntity<List<PortfolioDTO>> getAccountPortfolios(@PathVariable String accountId,
//			Authentication authentication) {
//		List<PortfolioDTO> portfolioDTOs = portfolioService.getPortfoliosByAccountId(accountId);
//		return ResponseEntity.ok(portfolioDTOs);
//	}

	@PatchMapping("/{accountId}/updatebalance")
//	@PreAuthorize("hasAuthority('STOCKADMIN')")
	public ResponseEntity<String> updateBalance(@PathVariable String accountId,
			@RequestBody BalanceUpdateDTO balanceUpdateDTO) {
		System.out.println(balanceUpdateDTO.getNewBalance());
		portfolioService.updateBalance(accountId, balanceUpdateDTO.getNewBalance());
		return ResponseEntity.ok("Balance updated successfully");
	}

//	@PostMapping("/{accountId}/new")
////	@PreAuthorize("hasAuthority('STOCKADMIN')")
//	public ResponseEntity<Void> addNewPortfolio(@PathVariable String accountId, @RequestBody PortfolioDTO portfolioDTO) {
//		portfolioDTO.setAccountId(accountId);
//		Portfolio portfolio = modelMapper.map(portfolioDTO, Portfolio.class);
//		portfolioService.addHolding(accountId, portfolio);
//		return new ResponseEntity<>(HttpStatus.CREATED);
//	}
}
