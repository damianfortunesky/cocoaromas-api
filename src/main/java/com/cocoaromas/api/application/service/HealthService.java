package com.cocoaromas.api.application.service;

import com.cocoaromas.api.application.port.in.GetHealthUseCase;
import com.cocoaromas.api.domain.HealthStatus;
import org.springframework.stereotype.Service;

@Service
public class HealthService implements GetHealthUseCase {

    @Override
    public HealthStatus getStatus() {
        return HealthStatus.UP;
    }
}
