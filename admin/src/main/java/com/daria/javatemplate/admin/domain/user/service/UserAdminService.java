package com.daria.javatemplate.admin.domain.user.service;

import com.daria.javatemplate.admin.domain.user.model.dto.UserAdminDTO;
import com.daria.javatemplate.admin.domain.user.model.dto.UserAdminDTO.UserLoginRequest;
import com.daria.javatemplate.core.common.exception.ApplicationErrorType;
import com.daria.javatemplate.core.common.exception.SilentApplicationErrorException;
import com.daria.javatemplate.core.config.security.jwt.token.JwtToken;
import com.daria.javatemplate.core.config.security.jwt.token.JwtTokenProvider;
import com.daria.javatemplate.core.domain.user.model.dto.UserDTO;
import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import com.daria.javatemplate.core.domain.user.model.mapper.UserMapper;
import com.daria.javatemplate.core.domain.user.service.UserService;
import com.daria.javatemplate.core.domain.user.type.UserRole;
import com.daria.javatemplate.core.domain.user.type.UserStatus;
import com.daria.javatemplate.core.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAdminService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    public UserEntity createUser(UserDTO userDto) {
        userDto.setUpwd(bCryptPasswordEncoder.encode(userDto.getUpwd()));
        return userService.createUser(userMapper.toEntity(userDto));
    }

    public JwtToken login(UserLoginRequest userLoginRequest) {
        UserEntity userEntity = userService.getUser(userLoginRequest.getProvider(), userLoginRequest.getProviderId());
        if (userEntity == null) {
            userEntity = userService.createUser(convertToUserEntity(userLoginRequest));
        }
        if(UserRole.USER.equals(userEntity.getUserRole()) == false || UserStatus.INACTIVE.equals(userEntity.getStatus())){
            throw new SilentApplicationErrorException(ApplicationErrorType.INACTIVE_USER);
        }
        return (jwtTokenProvider.generateToken(userEntity.getEmail()));
    }

    private UserEntity convertToUserEntity(UserLoginRequest userLoginRequest) {
        return UserEntity
                .builder()
                .email(userLoginRequest.getEmail())
                .status(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .upwd(bCryptPasswordEncoder.encode(userLoginRequest.getEmail()))
                .provider(userLoginRequest.getProvider())
                .providerId(userLoginRequest.getProviderId())
                .build();
    }
}
