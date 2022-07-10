package com.daria.javatemplate.core.config.security.service;

import com.daria.javatemplate.core.common.exception.AdminErrorType;
import com.daria.javatemplate.core.common.exception.SilentAdminErrorException;
import com.daria.javatemplate.core.config.security.config.PrincipleDetail;
import com.daria.javatemplate.core.domain.user.model.dto.UserDTO;
import com.daria.javatemplate.core.domain.user.model.entity.UserEntity;
import com.daria.javatemplate.core.domain.user.model.mapper.UserMapper;
import com.daria.javatemplate.core.domain.user.service.UserService;
import com.daria.javatemplate.core.domain.user.type.UserProvider;
import com.daria.javatemplate.core.domain.user.type.UserRole;
import com.daria.javatemplate.core.domain.user.type.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    private final UserMapper userMapper;

    // oauth 로그인 시 받은 userRequest 데이터에 대한 후처리 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User auth2User = super.loadUser(userRequest);
        String fullProvider = userRequest.getClientRegistration().getRegistrationId();
        UserProvider provider;
        try{
            provider = UserProvider.valueOf(fullProvider.toUpperCase());
        } catch (Exception e){
            provider = UserProvider.UNKNOWN;
        }
        String providerId;
        String email;
        if(UserProvider.GOOGLE.equals(provider)){
            providerId = auth2User.getAttribute("sub");
            email = auth2User.getAttribute("email");
        } else if (UserProvider.NAVER.equals(provider)){
            Map<String, String> response = auth2User.getAttribute("response");
            providerId = response.get("id");
            email = response.get("email");
        } else {
            throw new SilentAdminErrorException(AdminErrorType.UNKNOWN_USER);
        }

        UserEntity userEntity = userService.getUser(email);
        // 유저 데이터가 없을 경우 회원가입
        if (userEntity == null) {
            UserDTO userDTO = new UserDTO(
                    email,
                    bCryptPasswordEncoder.encode(email),
                    UserStatus.ACTIVE,
                    UserRole.USER,
                    provider,
                    providerId
            );
            userService.createUser(userMapper.toEntity(userDTO));
        }

        return new PrincipleDetail(userEntity, auth2User.getAttributes());
    }
}
