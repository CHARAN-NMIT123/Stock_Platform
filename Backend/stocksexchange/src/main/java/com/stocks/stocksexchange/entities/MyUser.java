package com.stocks.stocksexchange.entities;

import java.util.Random;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

	@PrePersist
	@PreUpdate
	private void generateAccountId() {
		if (fname.length() < 3 || lname.length() < 3) {
			throw new IllegalArgumentException("First name and Last name should be at least 3 characters long");
		}
		if (!email.endsWith("@cognizant.com")) {
			throw new IllegalArgumentException("Only Cognizant email ids are allowed");
		}

		if (this.accountId == null || this.accountId.isEmpty()) {
			String uniqueId = String.format("%04d", new Random().nextInt(10000));
			String prefix = lname.substring(0, 2).toUpperCase();
			this.accountId = prefix + uniqueId;
		}

	}

}
