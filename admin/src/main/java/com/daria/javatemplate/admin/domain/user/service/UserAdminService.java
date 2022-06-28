package com.daria.javatemplate.admin.domain.user.service;

import com.daria.javatemplate.core.domain.user.model.dto.UserDTO;
import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import com.daria.javatemplate.core.domain.user.model.mapper.UserMapper;
import com.daria.javatemplate.core.domain.user.service.UserService;
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

    public UserEntity createUser(UserDTO userDto) {
        userDto.setUpwd(bCryptPasswordEncoder.encode(userDto.getUpwd()));
        return userService.createUser(userMapper.toEntity(userDto));
    }
}
