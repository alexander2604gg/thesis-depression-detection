package com.alexandersaul.Alexander.config.security.filter;

import com.alexandersaul.Alexander.util.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtTokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 🔹 Si no hay token o no es Bearer → continuar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            DecodedJWT decodedJWT = jwtUtil.validateToken(token);

            String email = jwtUtil.extractEmail(decodedJWT);
            String authorities =
                    jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();

            Collection<? extends GrantedAuthority> authoritiesList =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            authoritiesList
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JWTVerificationException ex) {
            // 🔹 Token inválido → 401, NO 500
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/api/authentication")
                || path.startsWith("/api/user")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }

}
