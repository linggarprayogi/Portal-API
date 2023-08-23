package com.glovis.portal.security.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.glovis.portal.entity.UserRole;
import com.glovis.portal.repository.UserRepository;
import com.glovis.portal.repository.UserRoleRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

//	@Autowired
//	MemberRepository memberRepository;
//
//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//		Member user = memberRepository.findByEmail(email);
//
//		if (user != null && user.getEmail().equals(email)) {
//			return UserDetailsImpl.build(user);
//		} else {
//			throw new UsernameNotFoundException("User not found with username: " + email);
//		}
//
//	}
	
	 @Autowired
	 private UserRepository userRepository;

	 @Autowired
	 private UserRoleRepository userRoleRepository;

	 @Override
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 com.glovis.portal.entity.User appUser =
	                userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Login " +
	                        "Username Invalid."));

	        Set<GrantedAuthority> grantList = new HashSet<GrantedAuthority>();
	        List<UserRole> userRoles = userRoleRepository.findByUser(appUser);
	        for (UserRole role : userRoles) {
	            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleMst().getRoleName());
	            grantList.add(grantedAuthority);
	        }
	        UserDetails user = new User(username, appUser.getPassword(), grantList);

	        return user;
	 }
}
