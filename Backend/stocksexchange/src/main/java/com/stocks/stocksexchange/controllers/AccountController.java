package com.stocks.stocksexchange.controllers;

import java.util.List;

// import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.stocksexchange.dtos.AccountDTO;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.services.AccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/stocktrader")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepo accountRepo;

    @GetMapping
    @PreAuthorize("hasAuthority('STOCKADMIN')")
    public ResponseEntity<List<AccountDTO>> getAllAcc(Authentication authentication) {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TRADER')")
    public AccountDTO getAccById(@PathVariable("id") String accountId,Authentication authentication) {
    	
    	return accountService.getAccountById(accountId);
    }
    
    // Register a new trader and sending the account to other microservice
    @PostMapping("/new")
    public ResponseEntity<String> registerTrader(@Valid @RequestBody AccountDTO accountDTO) {
        accountService.registerAccount(accountDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('TRADER')")
    public ResponseEntity<String> updateAccount(@PathVariable String id, @RequestBody AccountDTO accountDTO, Authentication authentication, HttpServletRequest httpServletRequest ) {
        accountDTO.setAccountId(id); // Ensure the DTO has the correct account ID
        String token = httpServletRequest.getHeader("Authorization");
        AccountDTO updatedAccount = accountService.updateAccount(accountDTO, token);
        if (updatedAccount == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("Account updated successfully");
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STOCKADMIN')|| hasAuthority('TRADER')")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") String accountId, Authentication authentication){
    	accountService.deleteAccount(accountId);
    	return ResponseEntity.ok("Account Deleted successfully");
    }

}
