package kr.co.webee.presentation.support.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.webee.common.auth.jwt.JwtProvider;
import kr.co.webee.common.constant.JwtConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isNotValidAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveAccessToken(authHeader);
        authenticateUser(accessToken);

        filterChain.doFilter(request, response);
    }

    private boolean isNotValidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith(JwtConstants.ACCESS_TOKEN_HEADER_PREFIX);
    }

    private String resolveAccessToken(String authHeader) {
        String accessToken = resolveToken(authHeader);

        if (jwtProvider.isExpired(accessToken)) {
            throw new ExpiredJwtException(null, null, "");
        }

        return accessToken;
    }

    private void authenticateUser(String accessToken) {
        String userId = String.valueOf(jwtProvider.getUserId(accessToken));
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String resolveToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(JwtConstants.ACCESS_TOKEN_HEADER_PREFIX)) {
            return authHeader.substring(JwtConstants.ACCESS_TOKEN_HEADER_PREFIX.length());
        }
        return "";
    }
}
