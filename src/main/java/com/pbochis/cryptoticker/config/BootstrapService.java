package com.pbochis.cryptoticker.config;

import com.pbochis.cryptoticker.model.User;
import com.pbochis.cryptoticker.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BootstrapService  implements InitializingBean{

	@Autowired
	private UserRepository userRepository;


	@Override
	public void afterPropertiesSet() throws Exception {
		PasswordEncoder encoder = passwordEncoder();
		User user = new User("user", encoder.encode("user"));
		User admin = new User("admin", encoder.encode("admin"));

		userRepository.save(user);
		userRepository.save(admin);
	}

	@Bean
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
