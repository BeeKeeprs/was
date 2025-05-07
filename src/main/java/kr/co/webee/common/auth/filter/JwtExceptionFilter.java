package kr.co.webee.common.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.webee.common.error.ErrorType;
import kr.co.webee.presentation.response.ApiErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            setJwtExceptionResponse(response, ErrorType.EXPIRED_ACCESS_TOKEN);
        } catch (MalformedJwtException e) {
            setJwtExceptionResponse(response, ErrorType.MALFORMED_ACCESS_TOKEN);
        } catch (SignatureException e) {
            setJwtExceptionResponse(response, ErrorType.INVALID_ACCESS_TOKEN);
        } catch (JwtException e) {
            setJwtExceptionResponse(response, ErrorType.UNKNOWN_TOKEN_ERROR);
        }
    }

    private void setJwtExceptionResponse(HttpServletResponse response, ErrorType errorType) throws IOException {
        ApiErrorDto errorDto = ApiErrorDto.of(errorType,"");
        String messageBody = objectMapper.writeValueAsString(errorDto);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(messageBody);
    }
}
