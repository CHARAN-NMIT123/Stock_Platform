package com.stocks.orders.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account 
{   
	@jakarta.persistence.Id
	private String accountId;
	   
    @NotBlank(message = "First name is required")
    @Size(min = 3, message = "First name must be at least 3 characters")
    private String fname;
   
    @NotBlank(message = "Last name is required")
    @Size(min = 3, message = "Last name must be at least 3 characters")
    private String lname;
   
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = ".*@cognizant\\.com$", message = "Only Cognizant email ids are allowed")
    private String email;
   
    private boolean status;
    private String name;
    
    @OneToMany
    private List<Orders> orders = new ArrayList<>();
    
}
