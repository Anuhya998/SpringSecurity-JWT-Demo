package com.anu.securitydemo.config;

import com.anu.securitydemo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { // this class will extend for each request
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        log.info("JWT Auth filter obj created");
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("inside customized dofiler internal");
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")){
            log.info("either authheader is null or it doesn't start with bearer" +authHeader);
            filterChain.doFilter(request,response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        log.info("JWT TOKEN fetched from header: " + jwtToken);
        final String userName = jwtService.extractUserName(jwtToken);
        log.info("extracted user name from JWT service " + userName);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(userName != null && authentication ==null ){
            //Authentication
            log.info("inside customized authentication with JWT");
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            log.info("Fetched user details: " + "username: " + userDetails.getUsername() +"password: " +userDetails.getPassword() + "autorities: " +userDetails.getAuthorities());
            if (jwtService.isTokenValid(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource()
                                                    .buildDetails(request)
                );
                System.out.println("User Authorities: " + userDetails.getAuthorities());
                log.info("User Authorities: " + userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);


    }
}
