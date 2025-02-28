package com.stocks.Riskmanagement.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.Riskmanagement.dto.OrderDTO;
import com.stocks.Riskmanagement.entites.Orders;
import com.stocks.Riskmanagement.repository.OrderRepository;


@Service
public class orderService {

	
	 @Autowired
	    private OrderRepository orderRepository;

	    @Autowired
	    private ModelMapper modelMapper;

	    public OrderDTO getOrderDTO(OrderDTO orderDTO) {
	        // Convert OrderDTO to Order entity using ModelMapper
	        Orders order = modelMapper.map(orderDTO, Orders.class);
	        
	        // Save the order to the repository
	        order = orderRepository.save(order);
	        
	        // Convert the saved Order entity back to OrderDTO
	        OrderDTO updatedOrderDTO = modelMapper.map(order, OrderDTO.class);
	        
	        return updatedOrderDTO;
	    }
}
