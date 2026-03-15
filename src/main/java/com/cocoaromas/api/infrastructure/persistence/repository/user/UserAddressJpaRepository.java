package com.cocoaromas.api.infrastructure.persistence.repository.user;

import com.cocoaromas.api.infrastructure.persistence.entity.user.UserAddressEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressJpaRepository extends JpaRepository<UserAddressEntity, Long> {
    List<UserAddressEntity> findByUserIdAndActiveTrueOrderByUpdatedAtDesc(Long userId);
    Optional<UserAddressEntity> findByIdAndUserIdAndActiveTrue(Long id, Long userId);
}
