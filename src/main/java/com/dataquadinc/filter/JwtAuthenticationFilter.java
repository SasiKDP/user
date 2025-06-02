package com.dataquadinc.filter;

import com.dataquadinc.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userEmail = request.getHeader("X-Auth-User");
        log.info("==== USER SERVICE: Received X-Auth-User header: {} ====", userEmail);

        if (userEmail == null) {
            log.warn("==== USER SERVICE: No X-Auth-User header found. Skipping authentication. ====");
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("==== USER SERVICE: User already authenticated in security context ====");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            log.info("==== USER SERVICE: Loading user details for: {} ====", userEmail);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("==== USER SERVICE: Authentication set for user: {} ====", userEmail);

        } catch (Exception e) {
            log.error("==== USER SERVICE: Error authenticating user from gateway header: {} ====", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}