package com.daria.javatemplate.core.domain.user.service;

import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import com.daria.javatemplate.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
