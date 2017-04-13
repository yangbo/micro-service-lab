package org.yangbo.microservice.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangbo.microservice.user.entity.User;
import org.yangbo.microservice.user.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{id}")
	public User findById(@PathVariable Long id) {
		User user = userRepository.findOne(id);
		return user;
	}
	
	@GetMapping("/name/{name}")
	public List<User> findByNameLike(@PathVariable String name) {
		List<User> users = userRepository.findByNameLike('%'+name+'%');
		return users;
	}
	
	@GetMapping("/age/{age}")
	public List<User> findByAge(@PathVariable Short age) {
		List<User> users = userRepository.findByAge(age);
		return users;
	}
}
