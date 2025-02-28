package com.stocks.stocksexchange.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(unique = true, nullable = false)
    private String accountId;
    private String fname;
    private String lname;
    @Column(unique = true, nullable = false)
    private String email;
    private boolean status;
    @Transient
    private String name;

    public Account(String accountId, String fname, String lname, String email, boolean status, String name) {
    	super();
    	this.accountId = accountId;
    	this.fname = fname;
    	this.lname = lname;
    	this.email = email;
    	this.status = status;
    	this.name = name;
    }
 
    @PrePersist
    @PreUpdate
    public void generateName() {
    	this.name = fname + "" + lname;
    }

	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Portfolio portfolio;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trades> trades;

}
