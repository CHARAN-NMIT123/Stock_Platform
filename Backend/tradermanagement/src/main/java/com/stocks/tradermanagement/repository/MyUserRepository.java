package com.stocks.tradermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stocks.tradermanagement.entities.MyUser;

@Repository

public interface MyUserRepository extends JpaRepository<MyUser, String>

{

}
 
