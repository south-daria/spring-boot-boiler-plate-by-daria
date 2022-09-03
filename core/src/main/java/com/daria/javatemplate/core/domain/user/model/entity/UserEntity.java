package com.daria.javatemplate.core.domain.user.model.entity;

import com.daria.javatemplate.core.common.model.entity.BaseTimeEntity;
import com.daria.javatemplate.core.domain.user.type.UserProvider;
import com.daria.javatemplate.core.domain.user.type.UserRole;
import com.daria.javatemplate.core.domain.user.type.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@DynamicUpdate
@Table(name = "user")
public class UserEntity extends BaseTimeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "회원 번호")
    private Long id;

    @Column(name = "email", unique = true, columnDefinition = "회원 이메일")
    private String email;

    @Column(unique = true, length = 300)
    private String upwd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "회원 상태")
    private UserStatus status = UserStatus.INIT;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, columnDefinition = "회원 권한")
    private UserRole userRole = UserRole.ANONYMOUS;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, columnDefinition = "회원 권한")
    private UserProvider provider = UserProvider.ITSELF;

    @Column(name = "provider_id", columnDefinition = "provider 고유 id")
    private String providerId;

}
