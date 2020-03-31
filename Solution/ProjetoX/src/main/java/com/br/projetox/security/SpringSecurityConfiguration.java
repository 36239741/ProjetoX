package com.br.projetox.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.br.projetox.service.ImplementDetailService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private ImplementDetailService detail;
	
	
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
		.authorizeRequests()
		.antMatchers("/v1/contratos/salvar-biometria/**" ).permitAll()
		.antMatchers("/v1/contratos/ativos/**" ).permitAll()
		.antMatchers("/v1/registros/salvar-registro-entrada/**" ).permitAll()
		.antMatchers("/v1/registros/salvar-registro-saida/**" ).permitAll()
		.antMatchers("/v1/planos/listar-planos-por-contrato/**" ).permitAll()
		.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
		.anyRequest()
		.authenticated();
	}
	
	 @Override
     public void configure(WebSecurity web) throws Exception {
       web
         .ignoring()
            .antMatchers("/","/resources/**", "/*.js", "/*.png", "/assets/**"); 
       
     }
		

	
	
	
	
}