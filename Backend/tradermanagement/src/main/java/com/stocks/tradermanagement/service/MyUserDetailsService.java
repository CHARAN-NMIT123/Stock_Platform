package com.stocks.tradermanagement.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.stocks.tradermanagement.dtos.AccountDTO;
import com.stocks.tradermanagement.entities.MyUser;
import com.stocks.tradermanagement.repository.MyUserRepository;

@Service

public class MyUserDetailsService implements UserDetailsService

{

	private final String url="http://localhost:7070/api/traders/new";
	
	@Autowired

	private PasswordEncoder passwordEncoder;

	@Autowired

	private MyUserRepository ur;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<MyUser> temp = ur.findById(username);

		if(temp.isEmpty())

		{

			throw new UsernameNotFoundException(username);

		}

		MyUser myUser = temp.get();


		String str = myUser.getRoles();

		String[] roles = str.split(",");

		Collection<GrantedAuthority> authorities=new ArrayList<>();

		for(String role:roles)

		{

			authorities.add(new SimpleGrantedAuthority(role));

		}

		return new User(myUser.getUsername(), myUser.getPassword(), authorities);

	}

	public MyUser addNewUser(MyUser myUser)

	{

		String plainPassword=myUser.getPassword();

		String encPassword = passwordEncoder.encode(plainPassword);

		myUser.setPassword(encPassword);

		AccountDTO accountDTO = usertoAccount(myUser);
//		ResponseEntity<String> response = restTemplate.postForEntity(url, accountDTO, String.class);
		return ur.save(myUser);

	}
	
	public AccountDTO usertoAccount(MyUser myUser) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountId(myUser.getAccountId());
		accountDTO.setFname(myUser.getFname());
		accountDTO.setLname(myUser.getLname());
		accountDTO.setEmail(myUser.getEmail());

		return accountDTO;

	}
}
