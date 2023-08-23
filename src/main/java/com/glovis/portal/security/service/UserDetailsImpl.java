package com.glovis.portal.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glovis.portal.entity.User;

import lombok.Data;

@Data
public class UserDetailsImpl implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String            username;
	@JsonIgnore
	private String            password;

//	private String            email;

//	private List<String>	  roles;
//	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public static UserDetailsImpl build(User user) {
		return new UserDetailsImpl(user.getUsername(), user.getPassword());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
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
}
