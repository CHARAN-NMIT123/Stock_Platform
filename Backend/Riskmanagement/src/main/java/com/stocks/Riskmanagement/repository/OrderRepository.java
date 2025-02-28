package com.stocks.Riskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.Riskmanagement.entites.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>{

}
