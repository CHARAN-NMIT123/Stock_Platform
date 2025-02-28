package com.stocks.orders.service;

import com.stocks.orders.dto.OrderDTO;

public interface OrderService
{
    OrderDTO insertOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(int orderId);
    int processMarketSellOrder(OrderDTO orderDTO, String token);
    int processMarketBuyOrder(OrderDTO orderDTO, String token);
    int processStopLossOrder(OrderDTO orderDTO, String token);
    int processPositionSizingOrder(OrderDTO orderDTO, String token);
}
