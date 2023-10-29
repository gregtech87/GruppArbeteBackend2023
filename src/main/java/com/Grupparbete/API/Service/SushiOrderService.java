package com.Grupparbete.API.Service;

import com.Grupparbete.API.DTO.OrderItemDTO;
import com.Grupparbete.API.Entities.Dishes;
import com.Grupparbete.API.Entities.Order;
import com.Grupparbete.API.Entities.OrderDetails;

import java.util.List;

public interface SushiOrderService {
    Order saveOrder(Order order);
    Order createTakeawayOrder(int customerId, List<OrderItemDTO> orderItemDTOS);
    void deleteOrder(int id);
    Order findOrderById(int id);
    List<OrderDetails> findOrdersContainingDish(Dishes dish);
}

