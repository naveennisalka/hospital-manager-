package com.example.hospital_manager.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads "Authorization: Bearer <token>" on every request.
 * Valid token   -> the user (with the role stored in MongoDB) is logged in for this request.
 * Invalid token -> 401 straight away (tampered, expired, malformed).
 * No token      -> continue; protected URLs will answer 401.
 * Created in SecurityConfig (not a @Component) so it only runs inside the security chain.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ErrorResponseWriter errorWriter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        boolean isLogin = request.getRequestURI().equals("/api/auth/login");
        if (header == null || !header.startsWith("Bearer ") || isLogin) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            String username = jwtService.extractUsername(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (io.jsonwebtoken.SignatureException e) {
            errorWriter.write(response, HttpStatus.UNAUTHORIZED,
                    "Invalid JWT signature - the token has been modified", request.getRequestURI());
            return;
        } catch (ExpiredJwtException e) {
            errorWriter.write(response, HttpStatus.UNAUTHORIZED, "JWT token has expired", request.getRequestURI());
            return;
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            errorWriter.write(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token", request.getRequestURI());
            return;
        }

        chain.doFilter(request, response);
    }
}
