package org.yangbo.microservice.user.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.yangbo.microservice.user.entity.User;


public interface UserRepository extends CrudRepository<User, Long> {
	
	public List<User> findByAge(Short age);

	public List<User> findByNameLike(String name);
}
