package com.pbochis.cryptoticker.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "crypto_user")
public class User implements UserDetails {

	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> ga = new ArrayList<>();
		ga.add(() -> "ROLE_USER");
		if (this.username.equals("admin"))
			ga.add(() -> "ROLE_ADMIN");
		return ga;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return username;
	}
}
