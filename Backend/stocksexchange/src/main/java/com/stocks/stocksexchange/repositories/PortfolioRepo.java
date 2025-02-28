package com.stocks.stocksexchange.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocks.stocksexchange.entities.Portfolio;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {
	Portfolio findByAccountId(String accountId);
}
