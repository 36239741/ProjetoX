package com.br.projetox.test.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.br.projetox.ProjetoXApplication;
import com.br.projetox.ProjetoXApplicationTests;

@ActiveProfiles("henrique")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ProjetoXApplicationTests.class, ProjetoXApplication.class })
public abstract class AbstractIntegrationTest {
	@Before
	public void before() {
		
	}
}
