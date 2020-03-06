package com.br.projetox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import CIDBio.CIDBio;
import CIDBio.ConfigParam;


@SpringBootApplication
@EnableScheduling
public class ProjetoXApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoXApplication.class, args);	

	
		
}
	
	


}
