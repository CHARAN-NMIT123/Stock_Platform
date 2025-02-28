package com.stocks.orders.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.orders.dto.AccountDTO;
import com.stocks.orders.entities.Account;
import com.stocks.orders.repository.AccountRepository;

@Service
public class AccountService 
{	
	
	
	@Autowired
	private ModelMapper modelMapper; 
	@Autowired
	private AccountRepository accountRepo;
	
	public AccountDTO getAccountDTO(AccountDTO accountDTO)
	{
		Account account=modelMapper.map(accountDTO, Account.class);
		account=accountRepo.save(account);
		AccountDTO updatedAccountDTO=modelMapper.map(account, AccountDTO.class);
		return updatedAccountDTO;
	}
	
	public AccountDTO updateAccountDTO(AccountDTO accountDTO)
	{
		Account account=modelMapper.map(accountDTO, Account.class);
		account=accountRepo.save(account);
		AccountDTO updatedAccountDTO=modelMapper.map(account, AccountDTO.class);
		return updatedAccountDTO;
	}
	
	
	

}
