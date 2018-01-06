package com.pbochis.cryptoticker.security.filter;

import com.pbochis.cryptoticker.model.User;
import com.pbochis.cryptoticker.service.TokenAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//the jwt auth filter

public class AuthenticationFilter extends BasicAuthenticationFilter {

	private TokenAuthenticationService authenticationService;

	public AuthenticationFilter(AuthenticationManager authenticationManager, TokenAuthenticationService authenticationService) {
		super(authenticationManager);
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer")) {
			chain.doFilter(request, response);
			return;
		}

		User user = authenticationService.parseToken(header);
		if (user == null) {
			chain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		chain.doFilter(request, response);
	}
}
