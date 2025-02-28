package com.stocks.stocksexchange.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.stocksexchange.dtos.StockDTO;
import com.stocks.stocksexchange.entities.Stock;
import com.stocks.stocksexchange.exception.ResourceNotFoundException;
import com.stocks.stocksexchange.repositories.StockRepo;
import com.stocks.stocksexchange.repositories.TradesRepo;

@Service
public class StockService {
	@Autowired
	private StockRepo stockRepo;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private TradesRepo tradesRepo;

	public Stock updateStockStatus(Integer stockId, Boolean status) {
		Stock stock = stockRepo.findById(stockId).orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
		stock.setStatus(status);
		stockRepo.save(stock);
		return stock;
	}

	public List<StockDTO> getAllStocks() {
		return stockRepo.findAll().stream().map(stock -> modelMapper.map(stock, StockDTO.class))
				.collect(Collectors.toList());
	}

	public StockDTO registerStock(StockDTO stockDTO) {
		stockDTO.setStatus(true);
		Stock stock = modelMapper.map(stockDTO, Stock.class);
		Stock savedStock = stockRepo.save(stock);
		return modelMapper.map(savedStock, StockDTO.class);
	}

	public StockDTO updateStock(StockDTO stockDTO) {
//		stockDTO.setStockId((stockDTO.getStockId()));
		stockDTO.setStatus(true);
		Stock stock = modelMapper.map(stockDTO, Stock.class);
		Stock updatedStock = stockRepo.save(stock);
		return modelMapper.map(updatedStock, StockDTO.class);
	}

	public List<StockDTO> getStocksByType(String type) {
		return stockRepo.findByType(type).stream().map(stock -> modelMapper.map(stock, StockDTO.class))
				.collect(Collectors.toList());
	}
	
	public void deleteStock(int id) {
//		tradesRepo.deleteAllInBatch(tradesRepo.findByStockId(id));
		stockRepo.deleteById(id);
		
	}
}
