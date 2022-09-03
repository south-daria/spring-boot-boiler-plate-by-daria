package com.daria.javatemplate.admin.domain.user.model.dto;

import com.daria.javatemplate.core.domain.user.type.UserProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UserAdminDTO {
    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    @NoArgsConstructor
    public static class UserLoginRequest {
        private String email;
        @NotNull
        private UserProvider provider;
        @NotNull
        private String providerId;
    }
}
