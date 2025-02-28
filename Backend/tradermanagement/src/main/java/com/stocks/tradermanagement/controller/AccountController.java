package com.stocks.tradermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.tradermanagement.dtos.AccountDTO;
import com.stocks.tradermanagement.service.AccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/traders/")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/new")
// @PreAuthorize("hasAuthority('TRADER')")
    public ResponseEntity<Void> registerTrader(@RequestBody AccountDTO accountDTO) {
        accountService.getAccountDTO(accountDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/")
    @PreAuthorize("hasAuthority('STOCKADMIN')|| hasAuthority('TRADER')")
    public ResponseEntity<String> updateAccount(@RequestBody AccountDTO accountDTO, Authentication authentication) {
        accountService.updateAccountDTO(accountDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    
    }
}
