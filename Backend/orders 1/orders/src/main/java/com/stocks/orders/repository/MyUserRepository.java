package com.stocks.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocks.orders.entities.MyUser;

@Repository

public interface MyUserRepository extends JpaRepository<MyUser, String>

{

}
 
