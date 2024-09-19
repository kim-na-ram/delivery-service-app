package com.century21.deliveryserviceapp.entity;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.user.dto.request.SignUpRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Entity
@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @Column
    @NotNull
    private String password;

    @Column
    @NotNull
    private String nickname;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    private User(String email, String password, String nickname, Authority authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
    }

    public static User from(SignUpRequest signUpRequest) {
        return new User(
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getNickname(),
                Authority.getAuthority(signUpRequest.getAuthority())
        );
    }

    public void deleteUser() {
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }
}
