package com.cocoaromas.api.entrypoints.rest;

import com.cocoaromas.api.application.port.in.GetHealthUseCase;
import com.cocoaromas.api.domain.HealthStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Endpoints de disponibilidad del backend")
public class PingController {

    private final GetHealthUseCase getHealthUseCase;

    public PingController(GetHealthUseCase getHealthUseCase) {
        this.getHealthUseCase = getHealthUseCase;
    }

    @GetMapping("/ping")
    @Operation(summary = "Valida que el backend se encuentra operativo")
    public PingResponse ping() {
        HealthStatus status = getHealthUseCase.getStatus();
        return new PingResponse(status.name(), OffsetDateTime.now());
    }

    public record PingResponse(String status, OffsetDateTime timestamp) {
    }
}
