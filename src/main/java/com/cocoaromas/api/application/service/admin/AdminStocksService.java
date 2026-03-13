package com.cocoaromas.api.application.service.admin;

import com.cocoaromas.api.application.port.in.admin.AdminStocksUseCase;
import com.cocoaromas.api.application.port.out.admin.ManageAdminStocksPort;
import com.cocoaromas.api.domain.admin.stock.AdminStockDetail;
import com.cocoaromas.api.domain.admin.stock.AdminStockPage;
import com.cocoaromas.api.domain.admin.stock.AdminStockQuery;
import com.cocoaromas.api.domain.admin.stock.UpdateAdminStockCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminStocksService implements AdminStocksUseCase {

    private final ManageAdminStocksPort manageAdminStocksPort;
    private final int lowStockThreshold;

    public AdminStocksService(
            ManageAdminStocksPort manageAdminStocksPort,
            @Value("${app.stock.low-threshold:5}") int lowStockThreshold
    ) {
        this.manageAdminStocksPort = manageAdminStocksPort;
        this.lowStockThreshold = lowStockThreshold;
    }

    @Override
    public AdminStockPage list(AdminStockQuery query) {
        return manageAdminStocksPort.find(query, lowStockThreshold);
    }

    @Override
    public AdminStockDetail getByProductId(Long productId) {
        return manageAdminStocksPort.getByProductId(productId, lowStockThreshold);
    }

    @Override
    public AdminStockDetail updateStock(Long productId, UpdateAdminStockCommand command) {
        validate(command);

        AdminStockDetail current = manageAdminStocksPort.getByProductId(productId, lowStockThreshold);
        int targetQuantity = command.newStockQuantity() != null
                ? command.newStockQuantity()
                : current.stockQuantity() + command.adjustment();

        if (targetQuantity < 0) {
            throw new AdminStockValidationException("El stock no puede ser negativo");
        }

        return manageAdminStocksPort.updateSimpleStock(productId, targetQuantity, lowStockThreshold);
    }

    private void validate(UpdateAdminStockCommand command) {
        boolean hasSet = command.newStockQuantity() != null;
        boolean hasAdjustment = command.adjustment() != null;

        if (hasSet == hasAdjustment) {
            throw new AdminStockValidationException("Debes enviar exactamente uno de estos campos: newStockQuantity o adjustment");
        }

        if (hasSet && command.newStockQuantity() < 0) {
            throw new AdminStockValidationException("newStockQuantity no puede ser negativo");
        }

        if (hasAdjustment && command.adjustment() == 0) {
            throw new AdminStockValidationException("adjustment no puede ser 0");
        }
    }
}
