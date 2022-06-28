package com.daria.javatemplate.core.domain.user.model.entity;

import com.daria.javatemplate.core.domain.user.type.UserRole;
import com.daria.javatemplate.core.domain.user.type.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "회원 번호")
    private Long id;

    @Column(name = "email", unique = true, columnDefinition = "회원 이메일")
    private String email;

    @Column(unique = true, length = 300)
    private String upwd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "회원 상태")
    private UserStatus status = UserStatus.INIT;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, columnDefinition = "회원 권한")
    private UserRole userRole = UserRole.ANONYMOUS;

}
