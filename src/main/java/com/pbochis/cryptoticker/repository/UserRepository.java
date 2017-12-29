package com.pbochis.cryptoticker.repository;

import com.pbochis.cryptoticker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
