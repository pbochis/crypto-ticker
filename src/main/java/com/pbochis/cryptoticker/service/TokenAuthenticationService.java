package com.pbochis.cryptoticker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbochis.cryptoticker.model.User;
import com.pbochis.cryptoticker.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@Service
public class TokenAuthenticationService {

	private static final Long EXPIRATION_TIME = 8640000000L;
	private static final String SECRET = "mytokensecret";
	private static final String ISSUER = "itsMeMario";


	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public TokenAuthenticationService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User getAuthentication(HttpServletRequest request) {
		try {
			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			User existing = userRepository.findByUsername(user.getUsername());
			if (existing != null && passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
				return existing;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getToken(Principal principal) {
		return Jwts.builder()
				.setSubject(principal.getName())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
				.compact();
	}

	public User parseToken(String token) {
		if (token.startsWith("Bearer"))
			token = token.replace("Bearer ", "");
		Jws<Claims> jwt = Jwts.parser()
				.setSigningKey(SECRET.getBytes())
				.parseClaimsJws(token);
		// check expiry jwt.getBody().getExpiry...
		String username = jwt.getBody().getSubject();
		return userRepository.findByUsername(username);
	}
}
