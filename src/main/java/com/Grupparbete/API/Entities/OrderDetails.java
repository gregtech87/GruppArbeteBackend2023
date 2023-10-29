package com.Grupparbete.API.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dishes dish;

    @Column(name = "quantity")
    private int quantity;

//    @Column(name = "priceSEK", precision = 10, scale = 2)
    @Column(name = "priceSEK")
    private double priceSEK;

    @Column(name = "priceYEN")
    private int priceYEN;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Dishes getDish() {
        return dish;
    }

    public void setDish(Dishes dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceSEK() {
        return priceSEK;
    }

    public void setPriceSEK(double priceSEK) {
        this.priceSEK = priceSEK;
    }

    public int getPriceYEN() {
        return priceYEN;
    }

    public void setPriceYEN(int priceYEN) {
        this.priceYEN = priceYEN;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id=" + id +
                ", order=" + order +
                ", customer=" + customer +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", priceSEK=" + priceSEK +
                ", priceYEN=" + priceYEN +
                '}';
    }
}
