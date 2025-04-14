package com.htc.event.security;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration}")
	private int jwtExpirationMs;

	 
	 // Generate a JWT token with additional claims (e.g., roles).
	 
	public String generateToken(Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		// Extract roles from user details
		List<String> roles = userPrincipal.getAuthorities().stream().map(authority -> authority.getAuthority())
				.collect(Collectors.toList());

		return Jwts.builder().setSubject(userPrincipal.getUsername()).claim("roles", roles).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	 
	 // Extract a claim using a resolver function.
	  
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	 
	 // Extract username from JWT token.
	  
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	 
	// Extract all claims from the token.
	  
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			logger.warn("JWT token is expired: {}", e.getMessage());
			throw e; // Propagate the exception if required
		} catch (Exception e) {
			logger.error("Error parsing JWT token: {}", e.getMessage());
			throw new RuntimeException("Invalid JWT token", e);
		}
	}

	 
	 // Check if the token is expired.
	  
	public boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	 
	 // Validate JWT token by checking username and expiration.
	  
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	 
	 // Generate signing key from the secret.
	  
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
 