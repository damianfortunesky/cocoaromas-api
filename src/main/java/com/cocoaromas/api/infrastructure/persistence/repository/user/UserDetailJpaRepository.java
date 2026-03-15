package com.cocoaromas.api.infrastructure.persistence.repository.user;

import com.cocoaromas.api.infrastructure.persistence.entity.user.UserDetailEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailJpaRepository extends JpaRepository<UserDetailEntity, Long> {
    Optional<UserDetailEntity> findByUserId(Long userId);
}
