package com.pbochis.cryptoticker.security.filter;

import com.pbochis.cryptoticker.model.User;
import com.pbochis.cryptoticker.service.TokenAuthenticationService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private TokenAuthenticationService authenticationService;

	public LoginFilter(RequestMatcher requiresAuthenticationRequestMatcher, TokenAuthenticationService authenticationService) {
		super(requiresAuthenticationRequestMatcher);

		this.authenticationService = authenticationService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		User user = authenticationService.getAuthentication(request);
		if (user == null)
			throw new BadCredentialsException("Failed to authenticate");
		return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

		response.addHeader("Authorization",
				"Bearer " + authenticationService.getToken((Principal) authResult));

	}
}
