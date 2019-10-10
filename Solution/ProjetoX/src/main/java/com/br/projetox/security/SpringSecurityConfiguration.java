package com.br.projetox.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.br.projetox.jwt.JwtGeneration;
import com.br.projetox.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementDetailService detail;
	
	@Autowired
	private JwtGeneration jwtGeneration;
	
	@Autowired
	private UsuarioRepository repository;

	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
			return super.authenticationManager();
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return  PasswordEncoderFactories.createDelegatingPasswordEncoder();
	
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	http
	.authorizeRequests()
	.antMatchers("/authentication").permitAll()
	.anyRequest().authenticated()
	.and()
	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	.and()
	.addFilterBefore(new AuthenticacaoViaTokenFilter(jwtGeneration, repository), UsernamePasswordAuthenticationFilter.class);
	
	http.csrf().disable();
		
	}
		
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detail)
		.passwordEncoder(this.encoder());
		
	}
	
	
	
	
}