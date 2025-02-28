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

import com.stocks.stocksexchange.dtos.StockDTO;
import com.stocks.stocksexchange.entities.Stock;
import com.stocks.stocksexchange.repositories.StockRepo;
import com.stocks.stocksexchange.services.StockService;

@ExtendWith(MockitoExtension.class)
public class StocksServiceTest {

    @Mock
    private StockRepo stockRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStocks() {
        Stock stock1 = new Stock(1, "Stock A", "STKA", 1000, 10.0, 12.0, true, null);
        Stock stock2 = new Stock(2, "Stock B", "STKB", 2000, 20.0, 22.0, true, null);
        List<Stock> stocks = Arrays.asList(stock1, stock2);
        
        StockDTO stockDTO1 = new StockDTO(1, "Stock A", "STKA", 1000, 10.0, 12.0, true, "Tech");
        StockDTO stockDTO2 = new StockDTO(2, "Stock B", "STKB", 2000, 20.0, 22.0, true, "Finance");

        when(stockRepo.findAll()).thenReturn(stocks);
        when(modelMapper.map(stock1, StockDTO.class)).thenReturn(stockDTO1);
        when(modelMapper.map(stock2, StockDTO.class)).thenReturn(stockDTO2);

        List<StockDTO> stockDTOs = stockService.getAllStocks();

        Assertions.assertEquals(2, stockDTOs.size());
        Assertions.assertTrue(stockDTOs.contains(stockDTO1));
        Assertions.assertTrue(stockDTOs.contains(stockDTO2));
    }

    @Test
    void testRegisterStock() {
        StockDTO stockDTO = new StockDTO(3, "Stock C", "STKC", 1500, 15.0, 18.0, true, "Retail");
        Stock stock = new Stock(3, "Stock C", "STKC", 1500, 15.0, 18.0, true, null);
        Stock savedStock = new Stock(3, "Stock C", "STKC", 1500, 15.0, 18.0, true, null);
        StockDTO savedStockDTO = new StockDTO(3, "Stock C", "STKC", 1500, 15.0, 18.0, true, "Retail");

        when(modelMapper.map(stockDTO, Stock.class)).thenReturn(stock);
        when(stockRepo.save(stock)).thenReturn(savedStock);
        when(modelMapper.map(savedStock, StockDTO.class)).thenReturn(savedStockDTO);

        StockDTO result = stockService.registerStock(stockDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedStockDTO, result);
        verify(stockRepo, times(1)).save(stock);
    }

    @Test
    void testUpdateStock() {
        StockDTO stockDTO = new StockDTO(4, "Stock D", "STKD", 2500, 25.0, 28.0, true, "Healthcare");
        Stock stock = new Stock(4, "Stock D", "STKD", 2500, 25.0, 28.0, true, null);
        Stock updatedStock = new Stock(4, "Stock D", "STKD", 2500, 25.0, 28.0, true, null);
        StockDTO updatedStockDTO = new StockDTO(4, "Stock D", "STKD", 2500, 25.0, 28.0, true, "Healthcare");

        when(modelMapper.map(stockDTO, Stock.class)).thenReturn(stock);
        when(stockRepo.save(stock)).thenReturn(updatedStock);
        when(modelMapper.map(updatedStock, StockDTO.class)).thenReturn(updatedStockDTO);

        StockDTO result = stockService.updateStock(stockDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updatedStockDTO, result);
        verify(stockRepo, times(1)).save(stock);
    }

    @Test
    void testGetStocksByType() {
        String type = "Tech";
        Stock stock1 = new Stock(5, "Stock E", "STKE", 3000, 30.0, 35.0, true, null);
        Stock stock2 = new Stock(6, "Stock F", "STKF", 4000, 40.0, 45.0, true, null);
        List<Stock> stocks = Arrays.asList(stock1, stock2);
        
        StockDTO stockDTO1 = new StockDTO(5, "Stock E", "STKE", 3000, 30.0, 35.0, true, "Tech");
        StockDTO stockDTO2 = new StockDTO(6, "Stock F", "STKF", 4000, 40.0, 45.0, true, "Tech");

        when(stockRepo.findByType(type)).thenReturn(stocks);
        when(modelMapper.map(stock1, StockDTO.class)).thenReturn(stockDTO1);
        when(modelMapper.map(stock2, StockDTO.class)).thenReturn(stockDTO2);

        List<StockDTO> stockDTOs = stockService.getStocksByType(type);

        Assertions.assertEquals(2, stockDTOs.size());
        Assertions.assertTrue(stockDTOs.contains(stockDTO1));
        Assertions.assertTrue(stockDTOs.contains(stockDTO2));
    }

    @Test
    void testUpdateStockStatus() {
        Integer stockId = 7;
        Boolean status = true;
        
        Stock stock = new Stock(7, "Stock G", "STKG", 5000, 50.0, 55.0, false, null);
        when(stockRepo.findById(stockId)).thenReturn(Optional.of(stock));

        Stock result = stockService.updateStockStatus(stockId, status);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(status, result.isStatus());
        verify(stockRepo, times(1)).findById(stockId);
        verify(stockRepo, times(1)).save(stock);
    }
}
