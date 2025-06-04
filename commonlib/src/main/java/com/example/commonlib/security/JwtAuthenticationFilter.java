package com.example.commonlib.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements Filter {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        String header = http.getHeader(SecurityConstants.HEADER_STRING);

        if (header != null && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            String token = header.replace(SecurityConstants.TOKEN_PREFIX, "");

            if (jwtUtils.validateJwtToken(token)) {
                String id = jwtUtils.getUserIdFromJwtToken(token);
                String role = jwtUtils.getRoleFromJwtToken(token);

                var auth = new UsernamePasswordAuthenticationToken(
                        id, null, List.of(() -> "ROLE_" + role)
                );

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtils.getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                Double latitude = claims.get("latitude", Double.class);
                Double longitude = claims.get("longitude", Double.class);

                // Metti lat/lon in details o in un custom principal
                Map<String, Double> details = new HashMap<>();
                details.put("latitude", latitude);
                details.put("longitude", longitude);
                auth.setDetails(details);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
