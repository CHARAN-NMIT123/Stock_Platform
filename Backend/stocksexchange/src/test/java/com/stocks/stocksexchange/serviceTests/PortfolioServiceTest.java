package com.stocks.stocksexchange.serviceTests;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.stocks.stocksexchange.dtos.PortfolioDTO;
import com.stocks.stocksexchange.entities.Account;
import com.stocks.stocksexchange.entities.Portfolio;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.repositories.PortfolioRepo;
import com.stocks.stocksexchange.services.PortfolioService;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @Mock
    private PortfolioRepo portfolioRepo;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PortfolioService portfolioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetAllHoldings() {
//        Portfolio portfolio1 = new Portfolio(51L, "A1", 1000.0, true, null);
//        Portfolio portfolio2 = new Portfolio(52L, "A2", 2000.0, true, null);
//        List<Portfolio> portfolios = Arrays.asList(portfolio1, portfolio2);
//        
//        PortfolioDTO portfolioDTO1 = new PortfolioDTO(51L, "A1", 1000.0, true);
//        PortfolioDTO portfolioDTO2 = new PortfolioDTO(52L, "A2", 2000.0, true);
//
//        when(portfolioRepo.findAll()).thenReturn(portfolios);
//        when(modelMapper.map(portfolio1, PortfolioDTO.class)).thenReturn(portfolioDTO1);
//        when(modelMapper.map(portfolio2, PortfolioDTO.class)).thenReturn(portfolioDTO2);
//
//        List<PortfolioDTO> portfolioDTOs = portfolioService.getAllHoldings();
//
//        Assertions.assertEquals(2, portfolioDTOs.size());
//        Assertions.assertTrue(portfolioDTOs.contains(portfolioDTO1));
//        Assertions.assertTrue(portfolioDTOs.contains(portfolioDTO2));
//    }

    @Test
    void testAddHolding() {
        String accountId = "A1";
        Portfolio portfolio = new Portfolio(null, accountId, 1000.0, true, null);
        
        Account account = new Account();
        account.setAccountId(accountId);
        Portfolio savedPortfolio = new Portfolio(51L, accountId, 1000.0, true, account);
        PortfolioDTO portfolioDTO = new PortfolioDTO(51L, accountId, 1000.0, true);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(portfolioRepo.save(portfolio)).thenReturn(savedPortfolio);
        when(modelMapper.map(savedPortfolio, PortfolioDTO.class)).thenReturn(portfolioDTO);

        PortfolioDTO result = portfolioService.addHolding(accountId, portfolio);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(portfolioDTO, result);
        verify(accountRepo, times(1)).findById(accountId);
        verify(portfolioRepo, times(1)).save(portfolio);
    }

    @Test
    void testUpdateBalance() {
        String accountId = "A1";
        double newBalance = 1500.0;

        Portfolio portfolio = new Portfolio(51L, accountId, 1000.0, true, null);

        when(portfolioRepo.findByAccountId(accountId)).thenReturn(portfolio);

        portfolioService.updateBalance(accountId, newBalance);

        Assertions.assertEquals(newBalance, portfolio.getBalance());
        verify(portfolioRepo, times(1)).findByAccountId(accountId);
        verify(portfolioRepo, times(1)).save(portfolio);
    }


//    @Test
//    void testGetPortfoliosByAccountId() {
//        String accountId = "A1";
//        Portfolio portfolio1 = new Portfolio(51L, accountId, 1000.0, true, null);
//        Portfolio portfolio2 = new Portfolio(52L, accountId, 2000.0, true, null);
//        List<Portfolio> portfolios = Arrays.asList(portfolio1, portfolio2);
//        
//        PortfolioDTO portfolioDTO1 = new PortfolioDTO(51L, accountId, 1000.0, true);
//        PortfolioDTO portfolioDTO2 = new PortfolioDTO(52L, accountId, 2000.0, true);
//
//        when(portfolioRepo.findByAccountId(accountId)).thenReturn(portfolios);
//        when(modelMapper.map(portfolio1, PortfolioDTO.class)).thenReturn(portfolioDTO1);
//        when(modelMapper.map(portfolio2, PortfolioDTO.class)).thenReturn(portfolioDTO2);
//
//        List<PortfolioDTO> portfolioDTOs = portfolioService.getPortfoliosByAccountId(accountId);
//
//        Assertions.assertEquals(2, portfolioDTOs.size());
//        Assertions.assertTrue(portfolioDTOs.contains(portfolioDTO1));
//        Assertions.assertTrue(portfolioDTOs.contains(portfolioDTO2));
//    }
}
