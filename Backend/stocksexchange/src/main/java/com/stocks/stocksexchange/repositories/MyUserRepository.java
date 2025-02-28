package com.stocks.stocksexchange.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.stocks.stocksexchange.entities.MyUser;

@Repository

public interface MyUserRepository extends JpaRepository<MyUser, String>

{

}
 
