package com.stocks.tradermanagement.entities;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;

import jakarta.persistence.Table;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.ToString;

@Entity

@Table(name = "USERS100")

@NoArgsConstructor

@AllArgsConstructor

@Data

@ToString

public class MyUser {

	@Id
	private String username;

	private String password;

	private String roles;

	private String accountId;
	private String fname;
	private String lname;
	private String email;
}
 
