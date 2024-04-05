package com.services.InventoryService.services;

import com.services.InventoryService.dto.InventoryResponse;
import com.services.InventoryService.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> SkuCode){
        log.info("Checking Inventory");
        return inventoryRepository.findBySkuCodeIn(SkuCode).stream().map(inventory -> InventoryResponse.builder().SkuCode(inventory.getSkuCode()).isInStock(inventory.getQuantity()>0).build()).toList();

    }

}
