package io.incondensable.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.incondensable.application.business.domain.entity.Token;
import io.incondensable.application.business.domain.entity.User;
import io.incondensable.exception.BusinessException;
import io.incondensable.application.business.domain.enums.UserRoleTypes;
import io.incondensable.application.business.repository.UserRepository;
import io.incondensable.security.userdetails.MeniYuUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * @author abbas
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        extractJwtToken(request).ifPresentOrElse( // 1. extract and then look for the User with its JWT token.
                jwt -> {
                    String username = jwtUtil.getUserNameFromJwtToken(jwt);
                    try {
                        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) createAuthentication(username);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(request, response);
                    } catch (BusinessException e) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        try {
                            PrintWriter writer = response.getWriter();
                            objectMapper.writeValue(writer, "JWT Token is Expired.");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (ServletException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }, () -> {
                    try {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "failed to authorize.");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
        );
    }

    private Optional<String> extractJwtToken(HttpServletRequest request) {
        try {
            String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (Objects.nonNull(auth)) {
                if (auth.startsWith(BEARER_PREFIX)) {
                    String token = auth.substring(BEARER_PREFIX.length()).trim();
                    if (!token.isBlank() && jwtUtil.validateTokens(token)) {
                        return Optional.of(token);
                    }
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    private Authentication createAuthentication(String username) throws BusinessException {
        MeniYuUserDetails userDetails = (MeniYuUserDetails) userDetailsService.loadUserByUsername(username);

        if (userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(
                r -> r.equalsIgnoreCase(UserRoleTypes.CUSTOMER.toString())
        )) {
            validateJwtTokenExpiration(userDetails.getUser(), userDetails.getUser().getToken());
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    private void validateJwtTokenExpiration(User user, Token token) throws JwtTokenIsExpiredException {
        Instant now = Instant.now();
        if (token.getActiveTime().isBefore(now)) {
            user.setToken(null);
            userRepository.save(user);
            throw new JwtTokenIsExpiredException();
        }
        token.setActiveTime(Instant.from(now.plus(60, ChronoUnit.MINUTES)));
        user.setToken(token);
        userRepository.save(user);
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        String servletPath = request.getServletPath();

        if (servletPath.contains("/docs") || servletPath.contains("/api-docs") || servletPath.contains("/swagger-ui"))
            return true;

        if (servletPath.contains("/actuator"))
            return true;

        if (servletPath.contains("/login") || servletPath.contains("/logout") || servletPath.contains("otp"))
            return true;

        if (servletPath.contains("/authentication")) {
            if (servletPath.contains("/logout") || servletPath.contains("/change")) {
                return false;
            }
            return true;
        }

        return false;
    }
}