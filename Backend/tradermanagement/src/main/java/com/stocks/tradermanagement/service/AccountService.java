package com.stocks.tradermanagement.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.tradermanagement.dtos.AccountDTO;
import com.stocks.tradermanagement.entities.Account;
import com.stocks.tradermanagement.repository.AccountRepo;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ModelMapper modelMapper;


    
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
