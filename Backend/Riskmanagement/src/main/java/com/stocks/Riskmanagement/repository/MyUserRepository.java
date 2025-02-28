package com.stocks.Riskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.stocks.Riskmanagement.entites.MyUser;

@Repository

public interface MyUserRepository extends JpaRepository<MyUser, String>

{

}
 
