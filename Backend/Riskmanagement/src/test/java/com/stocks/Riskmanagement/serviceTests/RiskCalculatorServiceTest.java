package com.stocks.Riskmanagement.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.stocks.Riskmanagement.dto.RiskCalculatorDTO;
import com.stocks.Riskmanagement.entites.RiskCalculator;
import com.stocks.Riskmanagement.repository.RiskCalculatorRepository;
import com.stocks.Riskmanagement.service.RiskCalculatorService;

public class RiskCalculatorServiceTest {
	 
    @Mock
    private RiskCalculatorRepository riskCalculatorRepository;
 
    @Mock
    private ModelMapper modelMapper;
 
    @InjectMocks
    private RiskCalculatorService riskCalculatorService;
 
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCalculatePositionSizing() {
        // Arrange
        RiskCalculatorDTO riskCalculatorDTO = new RiskCalculatorDTO();
        riskCalculatorDTO.setOrderID(1L);
        riskCalculatorDTO.setAccBalance(10000);
        riskCalculatorDTO.setRiskPerTrade(500);
        riskCalculatorDTO.setStopLoss(100);
        riskCalculatorDTO.setEntryPrice(150);
        riskCalculatorDTO.setTransType("buy");
        riskCalculatorDTO.setTypeOfPurchase("marketPlan");
        riskCalculatorDTO.setTypeOfSell("stopLoss");

        RiskCalculator riskCalculator = new RiskCalculator();
        riskCalculator.setOrderID(1L);
        riskCalculator.setAccBalance(10000);
        riskCalculator.setRiskPerTrade(500);
        riskCalculator.setStopLoss(100);
        riskCalculator.setEntryPrice(150);
        riskCalculator.setTransType("buy");
        riskCalculator.setTypeOfPurchase("marketPlan");
        riskCalculator.setTypeOfSell("stopLoss");

        int riskPoint = (int) (riskCalculator.getEntryPrice() - riskCalculator.getStopLoss());
        double numOfShares = riskCalculator.getRiskPerTrade() / riskPoint;
        riskCalculator.setRiskPoint(riskPoint);
        riskCalculator.setNumOfShares(numOfShares);
        when(riskCalculatorRepository.save(riskCalculator)).thenReturn(riskCalculator);
        when(modelMapper.map(riskCalculator, RiskCalculatorDTO.class)).thenReturn(riskCalculatorDTO);

        // Act
        RiskCalculatorDTO result = riskCalculatorService.calculatePositionSizing(riskCalculatorDTO);

        // Assert
        assertEquals(riskPoint, result.getRiskPoint());
        assertEquals(numOfShares, result.getNumOfShares());
        verify(riskCalculatorRepository).save(riskCalculator);
        verify(modelMapper).map(riskCalculator, RiskCalculatorDTO.class);
    }
        @Test
    public void testHandleStopLoss() {
        // Arrange
        RiskCalculatorDTO riskCalculatorDTO = new RiskCalculatorDTO(
            "1", 1, 10000, 500, 100, 0, 10, "marketPlan", "stopLoss", 150.0, null, "buy"
        );
        double currentPrice = 90.0;
 
        RiskCalculator riskCalculator = new RiskCalculator(
            "1", 1L, 10000, 500, 100.0, 0, 10, "marketPlan", "stopLoss", 150.0, null, "buy"
        );
 
        when(modelMapper.map(riskCalculatorDTO, RiskCalculator.class)).thenReturn(riskCalculator);
        when(riskCalculatorRepository.save(riskCalculator)).thenReturn(riskCalculator);
        when(modelMapper.map(riskCalculator, RiskCalculatorDTO.class)).thenReturn(riskCalculatorDTO);
 
        // Act
        RiskCalculatorDTO result = riskCalculatorService.handleStopLoss(riskCalculatorDTO, riskCalculatorDTO.getEntryPrice());
 
        // Assert
        assertEquals(150.0, result.getEntryPrice());
        verify(modelMapper).map(riskCalculatorDTO, RiskCalculator.class);
        verify(riskCalculatorRepository).save(riskCalculator);
        verify(modelMapper).map(riskCalculator, RiskCalculatorDTO.class);
    }
}
