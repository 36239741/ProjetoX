package com.br.projetox.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import com.br.projetox.security.filter.handler.FailedHandler;
import com.br.projetox.security.filter.handler.SuccessHandler;
import com.br.projetox.service.ImplementDetailService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementDetailService detail;
	
	@Autowired
	private SuccessHandler successHandler;
	
	@Autowired
	private FailedHandler failedHandler;
	
	@Bean
	public PasswordEncoder encoder() {
		return  PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvaider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(detail);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvaider());
	}
	

	

	
	@Override	
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.httpBasic()
		.authenticationEntryPoint(new CustomEntryPoint())
		.and()
		.authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
		.anyRequest()
		.authenticated();
	}
		

	
	
	
	
}