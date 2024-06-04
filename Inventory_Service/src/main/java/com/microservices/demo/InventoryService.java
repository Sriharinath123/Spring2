package com.microservices.demo;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservices.demo.InventoryResponse.Builder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    
    
    //Pathvariable:-  http://localhost:8082/api/inventory/IPhone-15
    
    //Pathvariable:-  http://localhost:8082/api/inventory/IPhone-15, Iphone-13, Realme-12
     
    //RequestParam:-  http://localhost:8082/api/inventory?skuCode=IPhone-15&skuCode=Iphone-13&skuCode= Realme-12

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
    	
    	
        logger.info("Received SKU codes: {}", skuCodes);
        return inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory -> {
                    Builder builder = InventoryResponse.builder();
                    return builder
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build();
                })
                .collect(Collectors.toList());
    }

	public boolean isInStock(String skuCode) {
		// TODO Auto-generated method stub
		return false;
	}

	}
