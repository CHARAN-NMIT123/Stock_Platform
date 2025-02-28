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

import com.stocks.Riskmanagement.dto.OrderDTO;
import com.stocks.Riskmanagement.entites.Orders;
import com.stocks.Riskmanagement.repository.OrderRepository;
import com.stocks.Riskmanagement.service.orderService;

public class OrderServiceTest {
	@Mock
	private OrderRepository orderRepository;
 
	@Mock
	private ModelMapper modelMapper;
 
	@InjectMocks
	private orderService orderService;
 
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
 
	@Test
	public void testGetOrderDTO() {
		// Arrange
		OrderDTO orderDTO = new OrderDTO();
		Orders order = new Orders();
		when(modelMapper.map(orderDTO, Orders.class)).thenReturn(order);
		when(orderRepository.save(order)).thenReturn(order);
		when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
 
		// Act
		OrderDTO result = orderService.getOrderDTO(orderDTO);
 
		// Assert
		assertEquals(orderDTO, result);
		verify(modelMapper).map(orderDTO, Orders.class);
		verify(orderRepository).save(order);
		verify(modelMapper).map(order, OrderDTO.class);
	}
 
}