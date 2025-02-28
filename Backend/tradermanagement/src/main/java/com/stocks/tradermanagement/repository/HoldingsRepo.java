package com.stocks.tradermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stocks.tradermanagement.entities.Holdings;
@Repository
public interface HoldingsRepo extends JpaRepository<Holdings, Integer> {

    List<Holdings> findByAccountId(String accountId);

    // @Query("SELECT h.id FROM Holding h WHERE h.accountId = :accountId AND h.stockId = :stockId")
    // Integer findHoldingIdByAccountIdAndStockId(@Param("accountId") String accountId, @Param("stockId") int stockId);
    @Query("SELECT h.holdingId FROM Holdings h WHERE h.accountId = :accountId AND h.stockId = :stockId")
Integer findByAccountIdAndStockId(@Param("accountId") String accountId, @Param("stockId") int stockId);
}
