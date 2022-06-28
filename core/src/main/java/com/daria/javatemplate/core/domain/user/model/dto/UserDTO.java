package com.daria.javatemplate.core.domain.user.model.dto;

import com.daria.javatemplate.core.domain.user.type.UserStatus;
import com.daria.javatemplate.core.domain.user.type.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String upwd;
    private UserStatus status = UserStatus.ACTIVE;
    private UserRole userRole = UserRole.USER;
}
