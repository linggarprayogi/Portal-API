package com.glovis.portal.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.glovis.portal.pojo.LoginReqPojo;
import com.glovis.portal.pojo.LoginRspPojo;
import com.glovis.portal.pojo.ResponseModel;
import com.glovis.portal.pojo.UserRolePojo;
import com.glovis.portal.security.service.UserDetailsImpl;
import com.glovis.portal.security.service.UserDetailsServiceImpl;
import com.glovis.portal.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/auth")
public class AuthController {

	private static final Logger    LOG = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager  authenticationManager;
	@Autowired
	private JwtUtils               jwtUtils;
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginReqPojo payload) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(payload.getUsername(), payload.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);

			User 			principal    	= (User) authentication.getPrincipal();
			List<String> roles = new ArrayList<String>();
			for (GrantedAuthority a : principal.getAuthorities()) {
				roles.add(a.getAuthority());
			}
			UserRolePojo    userLogin  		= UserRolePojo.builder().username(principal.getUsername()).roles(roles).build();
			final String    accessToken  	= jwtUtils.generateJwtToken(authentication);
			final String    refreshToken 	= jwtUtils.generateRefreshJwtToken(authentication);

			LoginRspPojo        response     = new LoginRspPojo(userLogin, accessToken, refreshToken);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(true, "Success", response));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<ResponseModel> refreshToken(HttpServletRequest httpRequest) throws UsernameNotFoundException {
		HashMap<String, String> respons            = new HashMap<>();
		final String            requestTokenHeader = httpRequest.getHeader("Authorization");

		String                  token              = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer "))
			token = requestTokenHeader.substring(7);

		boolean valid = jwtUtils.validateJwtRefreshToken(token);
		if (!valid) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		String                  user           = jwtUtils.getUserNameFromJwtRefreshToken(token);
		User         userDetailsImpl = (User) userDetailsServiceImpl.loadUserByUsername(user);
		Authentication          authentication  = new UsernamePasswordAuthenticationToken(userDetailsImpl, null,
				userDetailsImpl.getAuthorities());
		String                  accessToken     = jwtUtils.generateJwtToken(authentication);

		HashMap<String, String> data            = new HashMap<>();
		data.put("accessToken", accessToken);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(true, "Success", data));
	}
}
