package org.yangbo.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("/")
	public String index() {
		return "你好，欢迎使用 Spring Boot Web!";
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "你好，欢迎使用 Spring Boot Web!";
	}
}
