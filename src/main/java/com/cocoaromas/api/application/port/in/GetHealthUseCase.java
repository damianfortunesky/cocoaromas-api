package com.cocoaromas.api.application.port.in;

import com.cocoaromas.api.domain.HealthStatus;

public interface GetHealthUseCase {
    HealthStatus getStatus();
}
