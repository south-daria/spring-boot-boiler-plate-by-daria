package com.daria.javatemplate.core.config.security.jwt.filter;

import com.hot6.phopa.core.common.exception.ApplicationErrorType;
import com.hot6.phopa.core.common.exception.SilentApplicationErrorException;
import com.hot6.phopa.core.domain.user.model.dto.UserDTO;
import com.hot6.phopa.core.domain.user.model.entity.UserEntity;
import com.hot6.phopa.core.domain.user.model.mapper.UserMapper;
import com.hot6.phopa.core.domain.user.service.UserService;
import com.hot6.phopa.core.security.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰을 생성 및 검증 모듈 클래스
    private final UserService userService;
    private final UserMapper userMapper;

    // Jwt Provider 주입

    // Request로 들어오는 Jwt Token의 유효성을 검증 (jwtTokenProvider.validateToken)하는
    // filter를 filterChain에 등록한다.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.getAuthToken(request);

        if (token != null && jwtTokenProvider.verifyToken(token)) {
            String email = jwtTokenProvider.getUid(token);
            Optional<UserEntity> userEntity = userService.getUserByStatus(email);
            if(userEntity.isEmpty()) {
                throw new SilentApplicationErrorException(ApplicationErrorType.UNAUTHORIZED_USER);
            }
            Authentication auth = getAuthentication(userMapper.toDto(userEntity.get()));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserDTO user) {
        return new UsernamePasswordAuthenticationToken(user, "",
                Arrays.asList(new SimpleGrantedAuthority(user.getUserRole().getAuthority())));
    }
}

