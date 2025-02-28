package com.stocks.Riskmanagement.config; 

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.stocks.Riskmanagement.filter.JwtAuthFilter;
import com.stocks.Riskmanagement.service.MyUserDetailsService;

@Configuration

@EnableWebSecurity

@EnableMethodSecurity

public class MySecurityConfiguration {

	@Autowired

	private MyUserDetailsService us;

	@Autowired

	private JwtAuthFilter jwtFilter;

	@Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    	return http

    			.csrf(csrf->csrf.disable())

    			.authorizeHttpRequests(auth->{

    				auth.requestMatchers("/v1/**","/swagger-ui/**","/swagger-resources/*","/v3/api-docs/**").permitAll();

    				auth.anyRequest().authenticated();

    			})

    			.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

    			.build();

    }
 
	@Bean

	public PasswordEncoder passwordEncoder()

	{

		return new BCryptPasswordEncoder();

	}

	@Bean

	public UserDetailsService userDetailsService()

	{

		return us;

	}

    @Bean

    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());

        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;

    }

    @Bean

    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();

    }

}
 
