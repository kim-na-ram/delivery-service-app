package com.century21.deliveryserviceapp.user.repository;

import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_USER;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일을 사용하여 사용자 정보를 조회하는 메서드
    Optional<User> findByEmail(String email);

    // 이메일이 이미 존재하는지 확인하는 메서드
    boolean existsByEmail(String email);

    Optional<User> findByIdAndDeletedAtIsNull(long id);

    default User findByUserId(long userId) {
        return findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

}
