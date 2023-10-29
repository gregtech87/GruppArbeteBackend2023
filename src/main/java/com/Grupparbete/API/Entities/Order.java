package com.Grupparbete.API.Entities;

import com.Grupparbete.API.Entities.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetails> orderDetails;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price_sek")
    private double totalPriceSEK;

    @Column(name = "total_price_yen")
    private int totalPriceYEN;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    public Order() {
    }

    public Order(Customer customer, List<OrderDetails> orderDetails) {
        this.customer = customer;
        this.orderDetails = orderDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPriceSEK() {
        return totalPriceSEK;
    }

    public void setTotalPriceSEK(double totalPriceSEK) {
        this.totalPriceSEK = totalPriceSEK;
    }

    public int getTotalPriceYEN() {
        return totalPriceYEN;
    }

    public void setTotalPriceYEN(int totalPriceYEN) {
        this.totalPriceYEN = totalPriceYEN;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", orderDetails=" + orderDetails +
                ", quantity=" + quantity +
                ", totalPriceSEK=" + totalPriceSEK +
                ", totalPriceYEN=" + totalPriceYEN +
                ", orderDate=" + orderDate +
                '}';
    }
}
