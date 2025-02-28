package com.stocks.stocksexchange.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.modelmapper.ModelMapper;
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

import com.stocks.stocksexchange.dtos.AccountDTO;
import com.stocks.stocksexchange.entities.Account;
import com.stocks.stocksexchange.entities.MyUser;
import com.stocks.stocksexchange.exception.ResourceNotFoundException;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.repositories.MyUserRepository;

@Service

public class MyUserDetailsService implements UserDetailsService

{

	private final String url = "http://localhost:9091/api/stocktrader/new";
	private final String url2 = "http://localhost:9090/v1/signup";
	private final String url3 = "http://localhost:7070/v1/signup";
	private final String url4 = "http://localhost:5050/v1/signup";

	@Autowired

	private PasswordEncoder passwordEncoder;

	@Autowired

	private MyUserRepository ur;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AccountRepo accountRepo;

	@Override

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<MyUser> temp = ur.findById(username);

		if (temp.isEmpty())

		{

			throw new UsernameNotFoundException(username);

		}

		MyUser myUser = temp.get();

		String str = myUser.getRoles();

		String[] roles = str.split(",");

		Collection<GrantedAuthority> authorities = new ArrayList<>();

		for (String role : roles)

		{

			authorities.add(new SimpleGrantedAuthority(role));

		}

		return new User(myUser.getUsername(), myUser.getPassword(), authorities);

	}

	public MyUser addNewUser(MyUser myUser)

	{

		String plainPassword = myUser.getPassword();

		String encPassword = passwordEncoder.encode(plainPassword);

		myUser.setPassword(encPassword);

		MyUser user = ur.save(myUser);
		AccountDTO accountDTO = usertoAccount(user);

		ResponseEntity<String> response = restTemplate.postForEntity(url, accountDTO, String.class);
		ResponseEntity<String> response2 = restTemplate.postForEntity(url2, user, String.class);
		ResponseEntity<String> response3 = restTemplate.postForEntity(url3, user, String.class);
		ResponseEntity<String> response4 = restTemplate.postForEntity(url4, user, String.class);
//		accountService.registerAccount(accountDTO);
		return user;

	}

	public AccountDTO usertoAccount(MyUser myUser) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountId(myUser.getAccountId());
		accountDTO.setFname(myUser.getFname());
		accountDTO.setLname(myUser.getLname());
		accountDTO.setEmail(myUser.getEmail());

		return accountDTO;
	}

	public String findIdByUsername(String username) {
		MyUser myUser = ur.findById(username).orElseThrow(() -> new ResourceNotFoundException("Username not valid"));
		return myUser.getAccountId();
	}

	public void login_updateAccountStatus(String id) {
		Account account = accountRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		account.setStatus(true);

		accountRepo.save(account);

	}

	public void logout_updateAccountStatus(String id) {
		// TODO Auto-generated method stub
		Account account = accountRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		account.setStatus(false);

		accountRepo.save(account);

	}

}
