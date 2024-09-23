package com.century21.deliveryserviceapp.user.repository;

import com.century21.deliveryserviceapp.common.exception.NotFoundException;
import com.century21.deliveryserviceapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.NOT_FOUND_USER;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndDeletedAtIsNull(long id);

    default User findByUserId(long userId) {
        return findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }
}
