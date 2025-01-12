package com.example.connectMates.filters;

import com.example.connectMates.dao.UserDao;
import com.example.connectMates.entities.User;
import com.example.connectMates.helper.UserPrincipal;
import com.example.connectMates.service.JwtServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtServices jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Hello from filter");

        // Check for "Bearer " prefix and presence of Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Extract username from JWT
                String username = jwtService.extractUsername(token);
                System.out.println(username);
                // Proceed only if user is not already authenticated
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load user details
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    System.out.println(userDetails);
                    // Validate token
                    if (jwtService.isTokenValid(token, userDetails)) {
                        // Set up Spring Security context with authenticated user
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println("This is hello from filter");
                    }
                }
            } catch (Exception e) {
                // Log the error if username extraction or token validation fails
               // System.out.println("*******"+SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
                SecurityContextHolder.clearContext();
                logger.error("JWT Authentication failed: " + e.getMessage());

            }
        }

        filterChain.doFilter(request, response);
    }
}
