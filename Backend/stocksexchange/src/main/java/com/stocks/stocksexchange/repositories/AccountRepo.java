package com.stocks.stocksexchange.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocks.stocksexchange.entities.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, String> {

}
