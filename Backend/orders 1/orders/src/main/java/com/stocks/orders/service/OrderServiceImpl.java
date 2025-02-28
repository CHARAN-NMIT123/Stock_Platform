package com.stocks.orders.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.stocks.orders.dto.OrderDTO;
import com.stocks.orders.entities.Account;
import com.stocks.orders.entities.Orders;
import com.stocks.orders.repository.AccountRepository;
import com.stocks.orders.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RestTemplate restTemplate;

	private final String stockurl = "http://localhost:9091/api/stock/getOders";
	private final String postionurl = "http://localhost:5050/api/risk/buy/positionSizing";
	private final String stopurl = "http://localhost:5050/api/risk/sell/stopLoss";
	private final String holdingBuyMarketUrl = "http://localhost:7070/api/holdings/buy/marketPlan";
	private final String holdingSellMarketUrl = "http://localhost:7070/api/holdings/sell/marketPlan";

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public OrderDTO getOrderById(int orderId) {
		Orders order = orderRepository.findById(orderId).orElse(null);
		if (order != null) {
			return convertToDTO(order);
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unused")
	public OrderDTO insertOrder(OrderDTO orderDTO) {

		// Business rules
		if (orderDTO.getStopLoss() < 0) {
			throw new IllegalArgumentException("StopLoss cannot be negative");
		}

		if ("positionSizing".equals(orderDTO.getTypeOfPurchase())) {
			orderDTO.setNumShares(0);
		}

		// Convert DTO to Entity
		Orders order = convertToEntity(orderDTO);
		Account account = accountRepository.findById(order.getAccountId())
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));
		order.setAccount(account);
		// Insert order
		Orders savedOrder = orderRepository.save(order);

		savedOrder.setAccount(null);

		// Convert Entity to DTO
		OrderDTO updatedOrderDTO = convertToDTO(savedOrder);

		OrderDTO response = restTemplate.postForObject(stockurl, updatedOrderDTO, OrderDTO.class);
		if (orderDTO.getTypeOfPurchase().equalsIgnoreCase("positionSizing")) {
			OrderDTO response2 = restTemplate.postForObject(postionurl, updatedOrderDTO, OrderDTO.class);
		} else if (orderDTO.getTypeOfPurchase().equalsIgnoreCase("stopLoss")) {
			OrderDTO response2 = restTemplate.postForObject(stopurl, updatedOrderDTO, OrderDTO.class);
		}
		return updatedOrderDTO;
	}

	@Override
	public OrderDTO updateOrder(OrderDTO orderDTO) {
		// Business rules
		if (orderDTO.getStopLoss() < 0) {
			throw new IllegalArgumentException("StopLoss cannot be negative");
		}

		if ("marketPlan".equals(orderDTO.getTypeOfSell())) {
			orderDTO.setStopLoss(0);
		}

		// Convert DTO to Entity
		Orders order = convertToEntity(orderDTO);

		// Update order
		Orders updatedOrder = orderRepository.save(order);

		// Convert Entity to DTO
		return convertToDTO(updatedOrder);
	}

	@Override
	public int processMarketSellOrder(OrderDTO orderDTO, String token) {
		if (orderDTO.getStopLoss() < 0) {
			throw new IllegalArgumentException("StopLoss cannot be negative");
		}
		if ("marketPlan".equals(orderDTO.getTypeOfSell())) {
			orderDTO.setStopLoss(0);
		}
		orderDTO.setTransStatus("success");
		orderDTO.setStatus(true);

		Orders order = convertToEntity(orderDTO);
		order.setTransType("sell");
		orderRepository.save(order);
		OrderDTO updatedOrderDTO = convertToDTO(order);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(updatedOrderDTO, headers);
		ResponseEntity<OrderDTO> response = restTemplate.postForEntity(stockurl, requestEntity, OrderDTO.class);
		ResponseEntity<Void> response1 = restTemplate.postForEntity(holdingSellMarketUrl, requestEntity, Void.class);

		return order.getNumShares();
	}

	@Override
	public int processMarketBuyOrder(OrderDTO orderDTO, String token) {
		if (orderDTO.getStopLoss() < 0) {
			throw new IllegalArgumentException("StopLoss cannot be negative");
		}
		if ("marketPlan".equals(orderDTO.getTypeOfSell())) {
			orderDTO.setStopLoss(0);
		}
		orderDTO.setTransStatus("success");
		orderDTO.setStatus(true);
		Orders order = convertToEntity(orderDTO);
		order.setTransType("buy");
		orderRepository.save(order);
		OrderDTO updatedOrderDTO = convertToDTO(order);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(updatedOrderDTO, headers);
		ResponseEntity<OrderDTO> response = restTemplate.postForEntity(stockurl, requestEntity, OrderDTO.class);
		ResponseEntity<Void> response1 = restTemplate.postForEntity(holdingBuyMarketUrl, requestEntity, Void.class);
		return order.getNumShares();
	}

	@Override
	public int processPositionSizingOrder(OrderDTO orderDTO, String token) {
		if (orderDTO.getStopLoss() < 0) {
			throw new IllegalArgumentException("StopLoss cannot be negative");
		}
		if ("marketPlan".equals(orderDTO.getTypeOfSell())) {
			orderDTO.setStopLoss(0);
		}
		orderDTO.setTransStatus("success");
		orderDTO.setStatus(true);

		Orders order = convertToEntity(orderDTO);
		order.setTransType("buy");
		order.setTypeOfPurchase("positionSizing");
		orderRepository.save(order);
		OrderDTO updatedOrderDTO = convertToDTO(order);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(updatedOrderDTO, headers);
		ResponseEntity<Void> response = restTemplate.postForEntity(postionurl, requestEntity, Void.class);
		return order.getNumShares();
	}

	@Override
	public int processStopLossOrder(OrderDTO orderDTO, String token) {
		if (orderDTO.getStopLoss() < 0) {
			throw new IllegalArgumentException("StopLoss cannot be negative");
		}
		if ("marketPlan".equals(orderDTO.getTypeOfSell())) {
			orderDTO.setStopLoss(0);
		}
		orderDTO.setTransStatus("success");
		orderDTO.setStatus(true);

		Orders order = convertToEntity(orderDTO);
		order.setTransType("SELL");
		// order.setTypeOfPurchase("StopLoss");
		orderRepository.save(order);
		OrderDTO updatedOrderDTO = convertToDTO(order);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(updatedOrderDTO, headers);
		OrderDTO response = restTemplate.postForObject(stopurl, requestEntity, OrderDTO.class);

		return order.getNumShares();
	}

	private Orders convertToEntity(OrderDTO orderDTO) {
		return modelMapper.map(orderDTO, Orders.class);
	}

	private OrderDTO convertToDTO(Orders order) {
		return modelMapper.map(order, OrderDTO.class);
	}
}