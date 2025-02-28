package com.stocks.stocksexchange.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.stocks.stocksexchange.dtos.AccountDTO;
import com.stocks.stocksexchange.entities.Account;
import com.stocks.stocksexchange.entities.Portfolio;
import com.stocks.stocksexchange.entities.Trades;
import com.stocks.stocksexchange.exception.ResourceNotFoundException;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.repositories.TradesRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AccountService {
	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TradesRepo tradesRepo;

	private final String url = "http://localhost:9090/api/orders/receiveaccount";
	private final String url2 = "http://localhost:7070/api/traders/new";

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private PortfolioService portfolioService;

	public List<AccountDTO> getAllAccounts() {
		return accountRepo.findAll().stream().map(account -> modelMapper.map(account, AccountDTO.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@SuppressWarnings("unused")
	public AccountDTO registerAccount(AccountDTO accountDTO) {
		accountDTO.setStatus(false);
//		System.out.println(accountDTO);
		Account account = modelMapper.map(accountDTO, Account.class);
		account = accountRepo.save(account);

		Portfolio portfolio = new Portfolio();
		portfolio.setAccountId(account.getAccountId());
		portfolio.setBalance(0);
		portfolio.setStatus(true);
		portfolio.setAccount(account);
		portfolioService.addHolding(account.getAccountId(), portfolio);

		AccountDTO updatedAccountDTO = modelMapper.map(account, AccountDTO.class);
//		PortfolioDTO portfolioDTO = new PortfolioDTO();
//		portfolioDTO.setAccountId(updatedAccountDTO.getAccountId());
//		portfolioDTO.setBalance(0);
//		portfolioDTO.setStatus(true);
//		ResponseEntity<Void> response3 = restTemplate.postForEntity("http://localhost:9091/api/portfolios/"+updatedAccountDTO.getAccountId()+"/new", portfolioDTO, Void.class);

		AccountDTO response = restTemplate.postForObject(url, updatedAccountDTO, AccountDTO.class);
		AccountDTO response2 = restTemplate.postForObject(url2, updatedAccountDTO, AccountDTO.class);

		return updatedAccountDTO;
	}

	@SuppressWarnings("unused")
	public AccountDTO updateAccount(AccountDTO accountDTO, String token) {
		if (accountDTO.getAccountId() == null) {
			throw new RuntimeException("Account not found");
		}
		accountDTO.setStatus(true);
		Account existingAccount = accountRepo.findById(accountDTO.getAccountId())
				.orElseThrow(() -> new RuntimeException("Account not found"));

		// Preserve existing trades
		List<Trades> existingTrades = existingAccount.getTrades();

		// Map DTO to entity
		Account account = modelMapper.map(accountDTO, Account.class);

		// Set the preserved trades back to the account entity
		account.setTrades(existingTrades);

		Account updatedAccount = accountRepo.save(account);
		AccountDTO updatedAccountDTO = modelMapper.map(updatedAccount, AccountDTO.class);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<AccountDTO> requestEntity = new HttpEntity<>(updatedAccountDTO, headers);

		ResponseEntity<AccountDTO> response = restTemplate.postForEntity(url, requestEntity, AccountDTO.class);
		ResponseEntity<AccountDTO> response2 = restTemplate.postForEntity(url2, requestEntity, AccountDTO.class);

		return updatedAccountDTO;
	}
//	@SuppressWarnings("unused")
//	public AccountDTO updateAccount(AccountDTO accountDTO, String token) {
//		if (accountDTO.getAccountId() == null) {
//			throw new RuntimeException("Account not found");
//		}
//		accountDTO.setStatus(true);
//		Account account = modelMapper.map(accountDTO, Account.class);
//		
//		Account updatedAccount = accountRepo.save(account);
//		AccountDTO updatedAccountDTO = modelMapper.map(updatedAccount, AccountDTO.class);
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Authorization", token);
//		HttpEntity<AccountDTO> requestEntity = new HttpEntity<>(updatedAccountDTO, headers);
//		ResponseEntity<AccountDTO> response = restTemplate.postForEntity(url, requestEntity, AccountDTO.class);
//		ResponseEntity<AccountDTO> response2 = restTemplate.postForEntity(url2, requestEntity, AccountDTO.class);
//		
//		return updatedAccountDTO;
//	}

	public AccountDTO getAccountById(String accountId) {
		Account account = accountRepo.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		return modelMapper.map(account, AccountDTO.class);
	}

	@Transactional
    public void deleteAccount(String accountId) {
        // Fetch the account
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        // Delete associated trades
        tradesRepo.deleteByAccountId(accountId);

        // Delete the account
        accountRepo.delete(account);
    }
}
