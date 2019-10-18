package com.br.projetox.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
	
	@Autowired
	private LogoutSuccessHandler logoutHandler;
	
	@Bean
	public PasswordEncoder encoder() {
		return  PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(detail);
		auth.setPasswordEncoder(this.encoder());
		return auth;
	}
	
	@Override	
	protected void configure(HttpSecurity http) throws Exception {
	http.csrf().disable()
	.httpBasic().disable()
	.formLogin()
	.permitAll()
	.loginProcessingUrl("/login")
	.usernameParameter("email")
	.passwordParameter("senha")
	.successHandler(successHandler)
	.failureHandler(failedHandler)
	.and()
	.logout()
	.permitAll()
	.invalidateHttpSession(true)
	.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "DELETE"))
	.invalidateHttpSession(true)
	.deleteCookies("JSESSIONID")
	.logoutSuccessHandler(logoutHandler)
	.and()
	.sessionManagement()
	.maximumSessions(1);
	
	http.authorizeRequests().anyRequest()
	.authenticated()
	.antMatchers("/login").permitAll();
	}
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.authenticationProvider());
	}
	
	
	
	
}