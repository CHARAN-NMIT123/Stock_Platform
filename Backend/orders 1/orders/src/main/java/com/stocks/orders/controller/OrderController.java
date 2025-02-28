package com.stocks.orders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stocks.orders.dto.AccountDTO;
import com.stocks.orders.dto.OrderDTO;
import com.stocks.orders.service.AccountService;
import com.stocks.orders.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private OrderService orderService;

	@Autowired
	private AccountService accounService;
 
	@PostMapping("/new")
	public OrderDTO insertOrder(@RequestBody OrderDTO orderDTO) {
		return orderService.insertOrder(orderDTO);
	}

	@PutMapping
	public OrderDTO updateOrder(@RequestBody OrderDTO orderDTO) {
		return orderService.updateOrder(orderDTO);
	}

	@PostMapping("/sell/MarketPlan")
	@PreAuthorize("hasAuthority('TRADER')")
	public ResponseEntity<String> sellOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest httpServletRequest,
			Authentication authentication) {
		String token = httpServletRequest.getHeader("Authorization");
		int unitsSold = orderService.processMarketSellOrder(orderDTO, token);
		if (unitsSold > 0) {
			return ResponseEntity.ok("Units sold: " + unitsSold);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sell order.");
		}
	}

	@PostMapping("/sell/stopLoss")
	@PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
	public ResponseEntity<String> stopLoss(@RequestBody OrderDTO orderDTO, HttpServletRequest httpServletRequest,
			Authentication authentication) {
		String token = httpServletRequest.getHeader("Authorization");
		int unitsSold = orderService.processStopLossOrder(orderDTO, token);
		if (unitsSold >= 0) {
			return ResponseEntity.ok("Units sold: " + unitsSold);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sell order.");
		}
	}

	@PostMapping("/buy/MarketPlan")
	@PreAuthorize("hasAuthority('TRADER')")
	public ResponseEntity<String> buyingOrder(@RequestBody OrderDTO orderDTO, Authentication authentication,
			HttpServletRequest httpServletRequest) {
		String token = httpServletRequest.getHeader("Authorization");
		System.out.println(orderDTO);
		int unitsBought = orderService.processMarketBuyOrder(orderDTO, token);
		if (unitsBought > 0) {
			return ResponseEntity.ok("Units bought: " + unitsBought);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sell order.");
		}
	}

	@PostMapping("/buy/positionSizing")
	@PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
	public ResponseEntity<String> positionSizing(@RequestBody OrderDTO orderDTO,HttpServletRequest httpServletRequest,
			Authentication authentication) {
		String token = httpServletRequest.getHeader("Authorization");
		int unitsBought = orderService.processPositionSizingOrder(orderDTO, token);
		if (unitsBought >= 0) {
			return ResponseEntity.ok("Units bought: " + unitsBought);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sell order.");
		}
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDTO> getOrderById(@PathVariable int orderId) {
		OrderDTO orderDTO = orderService.getOrderById(orderId);
		if (orderDTO != null) {
			return ResponseEntity.ok(orderDTO);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/receiveaccount")
	public ResponseEntity<AccountDTO> getAccount(@RequestBody AccountDTO accounDto) {
		accounService.getAccountDTO(accounDto);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("/updateaccount")
	@PreAuthorize("hasAuthority('STOCKADMIN')|| hasAuthority('TRADER')")
	public ResponseEntity<AccountDTO> updateAccount(@RequestBody AccountDTO accounDto) {
		accounService.updateAccountDTO(accounDto);
		return new ResponseEntity<>(HttpStatus.OK);

	}

}