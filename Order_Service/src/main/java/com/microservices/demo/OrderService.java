package com.microservices.demo;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public String placeOrder(OrderRequest orderRequest) {
        if (orderRequest == null || orderRequest.getOrderLineItemsDtoList() == null || orderRequest.getOrderLineItemsDtoList().isEmpty()) {
            throw new IllegalArgumentException("Order line items list is empty or null");
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(dto -> mapToDto(dto, order))
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        String inventoryServiceUrl = "http://localhost:8099/api/inventory?" +
                skuCodes.stream()
                        .map(skuCode -> "skuCode=" + skuCode)
                        .collect(Collectors.joining("&"));
        logger.info("Calling Inventory Service URL: {}", inventoryServiceUrl);

        try {
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri(inventoryServiceUrl)
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                logger.info("Order placed successfully.");
            } else {
                throw new IllegalArgumentException("Product is not in stock, please order after sometime");
            }
        } catch (Exception e) {
            logger.error("Error placing order: {}", e.getMessage());
        }
		return inventoryServiceUrl;
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto, Order order) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setOrder(order);
        return orderLineItems;
    
	}
    public void saveOrder(OrderDto orderDto) {
        Order order = new Order();
        // Map OrderDto to Order entity
        // Save order entity to the database
        orderRepository.save(order);
}
}