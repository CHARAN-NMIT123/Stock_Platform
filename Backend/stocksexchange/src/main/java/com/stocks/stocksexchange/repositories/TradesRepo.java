package com.stocks.stocksexchange.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocks.stocksexchange.entities.Trades;

@Repository
public interface TradesRepo extends JpaRepository<Trades, Integer> {

	List<Trades> findByAccountId(String accountId);

	List<Trades> findByAccountIdAndDateOfOrderAfter(String accountId, LocalDateTime date);

	List<Trades> findByStockId(int stockId);

	void deleteByAccountId(String accountId);

}
