package com.century21.deliveryserviceapp.user.repository;

import com.century21.deliveryserviceapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
