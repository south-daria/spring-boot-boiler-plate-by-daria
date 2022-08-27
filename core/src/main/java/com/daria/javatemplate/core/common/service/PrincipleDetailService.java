package com.daria.javatemplate.core.common.service;

import com.daria.javatemplate.core.common.exception.AdminErrorType;
import com.daria.javatemplate.core.common.exception.SilentAdminErrorException;
import com.daria.javatemplate.core.config.security.config.PrincipleDetail;
import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import com.daria.javatemplate.core.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipleDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(username);
        if(userEntity.isPresent()){
            return new PrincipleDetail(userEntity.get());
        }
        throw new SilentAdminErrorException(AdminErrorType.UNKNOWN_USER);
    }
}
