package com.stocks.Riskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stocks.Riskmanagement.dto.OrderDTO;
import com.stocks.Riskmanagement.dto.RiskCalculatorDTO;
import com.stocks.Riskmanagement.service.RiskCalculatorService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/risk")
public class RiskmanagementController {

	private final String holdingBuyPostionSizingUrl = "http://localhost:7070/api/holdings/buy/positionSizing";

	private final String holdingSellStopLossUrl = "http://localhost:7070/api/holdings/sell/stopLoss";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RiskCalculatorService riskCalculatorService;

	@PostMapping("/sell/stopLoss")
	@PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
	public ResponseEntity<Void> stopLoss(@RequestBody OrderDTO orderDTO, HttpServletRequest httpServletRequest, Authentication authentication ) {

		RiskCalculatorDTO riskCalculatorDTO = riskCalculatorService.processOrderToRisk(orderDTO);

		// Calculate position sizing using the service
		riskCalculatorService.handleStopLoss(riskCalculatorDTO, riskCalculatorDTO.getEntryPrice());
		System.out.println(orderDTO);
		
		String token = httpServletRequest.getHeader("Authorization");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(orderDTO, headers);
		ResponseEntity<OrderDTO> response = restTemplate.postForEntity("http://localhost:9091/api/stock/getOders", requestEntity,
				OrderDTO.class);
	ResponseEntity<String> response1 = restTemplate.postForEntity(holdingSellStopLossUrl, requestEntity, String.class);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/buy/positionSizing")
	@PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
	public ResponseEntity<Void> positionSizing(@RequestBody OrderDTO orderDTO, HttpServletRequest httpServletRequest, Authentication authentication) {

		RiskCalculatorDTO riskCalculatorDTO = riskCalculatorService.processOrderToRisk(orderDTO);
		// Calculate position sizing using the service
		RiskCalculatorDTO riskCalculatorDTO2 = riskCalculatorService.calculatePositionSizing(riskCalculatorDTO);
		orderDTO.setNumShares(riskCalculatorDTO2.getNumOfShares());
		String token = httpServletRequest.getHeader("Authorization");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(orderDTO, headers);
		ResponseEntity<OrderDTO> response = restTemplate.postForEntity("http://localhost:9091/api/stock/getOders", requestEntity,
				OrderDTO.class);
		ResponseEntity<String> response1 = restTemplate.postForEntity(holdingBuyPostionSizingUrl, requestEntity, String.class);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/v2/customer/home")

	@PreAuthorize("hasAuthority('RISKMANAGEMENT')")

	public String userHome(HttpServletRequest httpServletRequest,Authentication authentication) {

		return "Customer home page";

	}


}
