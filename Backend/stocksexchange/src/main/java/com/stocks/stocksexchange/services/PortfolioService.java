package com.stocks.stocksexchange.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.stocksexchange.dtos.PortfolioDTO;
import com.stocks.stocksexchange.entities.Account;
import com.stocks.stocksexchange.entities.Portfolio;
import com.stocks.stocksexchange.exception.ResourceNotFoundException;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.repositories.PortfolioRepo;

@Service
public class PortfolioService {
	@Autowired
	private PortfolioRepo portfolioRepo;

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private ModelMapper modelMapper;

//	public List<PortfolioDTO> getAllHoldings() {
//		return portfolioRepo.findAll().stream().map(portfolio -> modelMapper.map(portfolio, PortfolioDTO.class))
//				.collect(Collectors.toList());
//	}

	public PortfolioDTO addHolding(String accountId, Portfolio portfolio) {
		Account account = accountRepo.findById(portfolio.getAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		portfolio.setAccountId(accountId);
		portfolio.setAccount(account);
		Portfolio savedPortfolio = portfolioRepo.save(portfolio);
		return modelMapper.map(savedPortfolio, PortfolioDTO.class);
	}

	public void updateBalance(String accountId, double newBalance) {
		Portfolio portfolio = portfolioRepo.findByAccountId(accountId);
		portfolio.setBalance(newBalance);
		portfolioRepo.save(portfolio);
	}

//	public List<PortfolioDTO> getPortfoliosByAccountId(String accountId) {
//		return portfolioRepo.findByAccountId(accountId).stream()
//				.map(portfolio -> modelMapper.map(portfolio, PortfolioDTO.class)).collect(Collectors.toList());
//	}

}
