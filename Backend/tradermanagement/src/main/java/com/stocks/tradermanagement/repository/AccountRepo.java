package com.stocks.tradermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocks.tradermanagement.entities.Account;
@Repository
public interface AccountRepo extends JpaRepository<Account, String> {
    
}
