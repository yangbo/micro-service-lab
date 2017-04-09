package org.yangbo.springboot.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class HelloControllerIT {
	
	@LocalServerPort
	private int port;
	
	private URI base;
	
	@Autowired
	private TestRestTemplate template;
	
	@Before
	public void before() throws Exception{
		this.base = new URI("http://localhost:" + this.port);
	}
	
	@Test
	public void testHello() {
		ResponseEntity<String> response = template.getForEntity(this.base, String.class);
		assertThat(response.getBody(), equalTo("你好，欢迎使用 Spring Boot Web!"));
	}
}
