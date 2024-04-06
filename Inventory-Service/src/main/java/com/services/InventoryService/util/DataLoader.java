package com.services.InventoryService.util;

import com.services.InventoryService.dto.InventoryResponse;
import com.services.InventoryService.model.Inventory;
import com.services.InventoryService.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("iphone_12");
        inventory1.setQuantity(100);

        Inventory inventory2 = new Inventory();
        inventory2.setSkuCode("iphone_13");
        inventory2.setQuantity(0);

        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);
    }
}
