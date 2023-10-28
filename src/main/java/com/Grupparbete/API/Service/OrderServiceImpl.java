package com.Grupparbete.API.Service;

import com.Grupparbete.API.CurrencyConverter;
import com.Grupparbete.API.DAO.OrderRepository;
import com.Grupparbete.API.DTO.OrderItemDTO;
import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.Dishes;
import com.Grupparbete.API.Entities.Order;
import com.Grupparbete.API.Entities.OrderDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private CustomerService customerService;
    private DishesService dishesService;


    @Autowired
    public OrderServiceImpl(OrderRepository ordRepository, CustomerService custService, DishesService dishService){
        orderRepository = ordRepository;
        customerService = custService;
        dishesService = dishService;
    }


    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order createTakeawayOrder(int customerId, List<OrderItemDTO> orderItemDTOS) {
        Customer customer = customerService.findCustomerById(customerId);

        if (customer == null) {
            throw new IllegalArgumentException("Felaktigt kund-ID.");
        }

        List<OrderDetails> orderDetailsList = new ArrayList<>();
        double totalSEKPrice = 0.0;
        int totalYENPrice = 0;

        Order order = new Order(customer, new ArrayList<>());

        int quantity = 0;
        int dishQuantity = 0;
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            int dishId = orderItemDTO.getDishId();
            quantity = orderItemDTO.getQuantity();

            Dishes dish = dishesService.findDishById(dishId);
            if (dish == null) {
                throw new IllegalArgumentException("Felaktigt rätt-ID: " + dishId);
            }


            double dishSEKPrice = dish.getSekPrice() * quantity;
            totalSEKPrice += dishSEKPrice;
            totalYENPrice += dishSEKPrice * converter.getSEKToYenExchangeRate();

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setCustomer(customer);
            orderDetails.setDish(dish);
            orderDetails.setQuantity(quantity);
            orderDetails.setPriceSEK(dishSEKPrice);
            try{
            orderDetails.setPriceYEN((int) CurrencyConverter.SekToRequestedCurrency(dishSEKPrice, "JPY"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            orderDetails.setOrder(order);

            orderDetailsList.add(orderDetails);
            dishQuantity += quantity;
        }

        order.setOrderDetails(orderDetailsList);
        order.setTotalPriceSEK(totalSEKPrice);
        order.setTotalPriceYEN(totalYENPrice);
        order.setQuantity(dishQuantity);
        order.setOrderDate(LocalDateTime.now());

        orderRepository.save(order);

        return order;
    }


    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<OrderDetails> findOrdersContainingDish(Dishes dish) {
        return orderRepository.findOrderDetailsContainingDish(dish);
    }

    @Override
    public Order findOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

}
