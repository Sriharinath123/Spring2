package com.microservices.demo;

import java.util.List;

public class OrderRequest {
    private List<OrderLineItemsDto> orderLineItemsDtoList;

    // Getters and setters
    public List<OrderLineItemsDto> getOrderLineItemsDtoList() {
        return orderLineItemsDtoList;
    }

    public void setOrderLineItemsDtoList(List<OrderLineItemsDto> orderLineItemsDtoList) {
        this.orderLineItemsDtoList = orderLineItemsDtoList;
    }
}
