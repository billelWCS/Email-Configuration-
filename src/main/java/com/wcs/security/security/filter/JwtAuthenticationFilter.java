package com.wcs.security.security.filter;

import com.wcs.security.serviceImplem.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getContextPath().startsWith("/auth")){
            filterChain.doFilter(request, response);
        }else {
            String authHeader = request.getHeader("Authorization");
            String jwt;
            String userEmail;
            if (authHeader == null || !authHeader.startsWith("Bearer")){
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring("Bearer".length());
            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
