package com.daria.javatemplate.core.domain.user.model.dto;

import com.daria.javatemplate.core.domain.user.type.UserProvider;
import com.daria.javatemplate.core.domain.user.type.UserStatus;
import com.daria.javatemplate.core.domain.user.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String upwd;
    private UserStatus status;
    private UserRole userRole;
    private UserProvider userProvider;
    private String providerId;
}
