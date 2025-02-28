package com.stocks.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.orders.entities.Account;

public interface AccountRepository extends JpaRepository<Account,String>{

}
