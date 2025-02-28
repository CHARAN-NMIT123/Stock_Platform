package com.stocks.orders.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.orders.entities.MyUser;
import com.stocks.orders.helper.AuthRequest;
import com.stocks.orders.helper.MyToken;
import com.stocks.orders.service.JwtService;
import com.stocks.orders.service.MyUserDetailsService;
 
@RestController

public class UserController {

	@Autowired

	private MyUserDetailsService us;

	@Autowired

	private AuthenticationManager authenticationManager;

	@Autowired

	private JwtService jwtService;
 
	@GetMapping("/v1/home")

	public String home() {

		return "Everybody is welcome";

	}
 
	@GetMapping("/v2/customer/home")

	@PreAuthorize("hasAuthority('CUSTOMER')")

	public String userHome() {

		return "Customer home page";

	}
 
	@GetMapping("/v2/stockadmin/home")

	@PreAuthorize("hasAuthority('STOCKADMIN')")

	public String adminHome() {

		return "Hi Stock Admin, welcome";

	}
 
	@PostMapping("/v1/signup")

	public MyUser signup(@RequestBody MyUser myUser) {
 
		return us.addNewUser(myUser);

	}
 
	@PostMapping("/v1/login")

	public MyToken login(@RequestBody AuthRequest authRequest)

	{

		MyToken token=new MyToken();

		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		if(auth.isAuthenticated())

		{

			String jwt=jwtService.generateToken(authRequest.getUsername());

			token.setUsername(authRequest.getUsername());

			token.setToken(jwt);

			token.setAuthorities(auth.getAuthorities());

		}else

		{

			throw new UsernameNotFoundException("Login failed");

		}

		return token;

	}
 
}
 
