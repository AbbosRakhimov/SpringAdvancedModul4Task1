package uz.pdp.securityconfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import uz.pdp.service.MyAuthService;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	MyAuthService myAuthService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		
		String token= request.getHeader("Authorization");
		
		System.out.println(token);
		
		if(token!=null&& token.startsWith("Bearer")) {
			token = token.substring(7);
			boolean valoidetoken= jwtProvider.validateToken(token);
			if(valoidetoken) {
				String username=jwtProvider.getNameFromToken(token);
				UserDetails userDetails= myAuthService.loadUserByUsername(username);
				if(userDetails!=null) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =	new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}else {
					response.sendError(404);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	
}
