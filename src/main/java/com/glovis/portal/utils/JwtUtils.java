package com.glovis.portal.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.glovis.portal.security.service.UserDetailsImpl;
import com.glovis.portal.security.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils implements Serializable {

	private static final Logger    logger             = LoggerFactory.getLogger(JwtUtils.class);

	@Autowired
	private UserDetailsServiceImpl jwtUserDetailsService;

	private static final long      serialVersionUID   = -2550185165626007488L;

	public static final long       JWT_TOKEN_VALIDITY = 100000000;

	private static final long      ACCESS_TOKEN_EXP   = 300;

	private static final long      REFRESH_TOKEN_EXP  = 86400;

	@Value("${jwt.secret}")
	private String                 jwtSecret;
	@Value("${jwt.secret.refresh}")
	private String                 jwtSecretRefresh;
	@Value("${jwt.secret.otp}")
	private String                 jwtSecretOtp;
	@Value("${jwt.refreshExpiration}")
	private int                    refreshJwtExpiration;
	@Value("${jwt.expiration}")
	private int                    jwtExpiration;
	@Value("${jwt.otpExpiration}")
	private int                    otpJwtExpiration;

//	Gson                           gson               = new Gson();

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public boolean validateJwtRefreshToken(String refreshToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecretRefresh).parseClaimsJws(refreshToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public boolean validateJwtOTPToken(String otpToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecretOtp).parseClaimsJws(otpToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateJwtToken(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
//		// set payload akses token
//		PayloadAccess payloadAccess   = PayloadAccess.builder()
//										.username(principal.getUsername())
//										.email(principal.getEmail()).build();
//		String        payloadAccesStr = gson.toJson(payloadAccess);
		Calendar        calendar  = Calendar.getInstance();
		calendar.add(Calendar.SECOND, jwtExpiration);
		Date expireDate = calendar.getTime();

		return Jwts.builder().setSubject((principal.getUsername())).setIssuedAt(new Date()).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String generateRefreshJwtToken(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		Calendar        calendar  = Calendar.getInstance();
		calendar.add(Calendar.SECOND, refreshJwtExpiration);
		Date expireDate = calendar.getTime();
		return Jwts.builder().setSubject((principal.getUsername())).setIssuedAt(new Date()).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecretRefresh).compact();
	}

	public String generateOTPJwtToken(String email) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, otpJwtExpiration);
		Date expireDate = calendar.getTime();
		return Jwts.builder().setSubject((email)).setIssuedAt(new Date()).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecretOtp).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public String getUserNameFromJwtRefreshToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecretRefresh).parseClaimsJws(token).getBody().getSubject();
	}

	public String getUserNameFromJwtOTPToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecretOtp).parseClaimsJws(token).getBody().getSubject();
	}
}
