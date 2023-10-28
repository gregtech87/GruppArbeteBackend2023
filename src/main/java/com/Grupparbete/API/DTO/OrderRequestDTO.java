package com.Grupparbete.API.DTO;

import com.Grupparbete.API.DTO.OrderItemDTO;

import java.util.List;

public class OrderRequestDTO {
    private int customerId;
    private List<OrderItemDTO> orders;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItemDTO> orders) {
        this.orders = orders;
    }
}

