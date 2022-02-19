package uz.pdp.securityconfig;


import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	
	long expireTime=1000000000l;
	String secret="FristJwtlearningKours";
	
	public String generateToken(String username) {
		String token = Jwts
					.builder()
					.setSubject(username)
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis()+expireTime))
					.signWith(SignatureAlgorithm.HS512, secret)
					.compact();
		return token;
	}
	
	public boolean validateToken(String token) {
		
		try {
			Jwts
			.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getNameFromToken(String token) {
		String username=Jwts
			.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
		return username;
	}
}
