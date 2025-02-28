package com.stocks.stocksexchange.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.stocks.stocksexchange.dtos.StatusUpdateDTO;
import com.stocks.stocksexchange.dtos.StockDTO;
import com.stocks.stocksexchange.services.StockService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/stocks")
public class StockController {

	@Autowired
	private StockService stockService;

	@GetMapping("/companies/type")
	@PreAuthorize("hasAuthority('STOCKADMIN')|| hasAuthority('TRADER')")
	public ResponseEntity<List<StockDTO>> getStocksByType(@RequestParam String type, Authentication authentication) {
		List<StockDTO> stocks = stockService.getStocksByType(type);
		return new ResponseEntity<>(stocks, HttpStatus.OK);
	}

	@GetMapping
	@PreAuthorize("hasAuthority('STOCKADMIN')|| hasAuthority('TRADER')")
	public ResponseEntity<List<StockDTO>> getAllStocks(Authentication authentication) {
		List<StockDTO> stocks = stockService.getAllStocks();
		return new ResponseEntity<>(stocks, HttpStatus.OK);
	}

	@PostMapping("/companies/new")
	@PreAuthorize("hasAuthority('STOCKADMIN')")
	public ResponseEntity<Void> registerNewStock(@RequestBody StockDTO stockDTO, Authentication authentication) {
		System.out.println(stockDTO);
		stockService.registerStock(stockDTO);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/companies/{stockId}/update")
	@PreAuthorize("hasAuthority('STOCKADMIN')")
	public ResponseEntity<Void> updateCompanyInfo(@PathVariable Integer stockId, @RequestBody StockDTO stockDTO,
			Authentication authentication) {
		stockDTO.setStockId(stockId);
		stockService.updateStock(stockDTO);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping("/{id}/status")
	@PreAuthorize("hasAuthority('STOCKADMIN')")
	public ResponseEntity<Void> updateStockStatus(@PathVariable Integer id, @RequestBody StatusUpdateDTO newStatus,
			Authentication authentication) {
		stockService.updateStockStatus(id, newStatus.isNewStatus());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/{id}/delete")
	@PreAuthorize("hasAuthority('STOCKADMIN')")
	public void deleteStock(@PathVariable Integer id, Authentication authentication) {
		stockService.deleteStock(id);
	}
}
