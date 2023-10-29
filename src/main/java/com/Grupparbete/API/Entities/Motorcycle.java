package com.Grupparbete.API.Entities;

import jakarta.persistence.*;

@Entity
@Table(name="motorcycle")
public class Motorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="brand")
    private String brand;

    @Column(name="model")
    private String model;

    @Column(name="price_per_day_in_sek")
    private int pricePerDayInSek;

    @Column(name="is_rented")
    private boolean rented;

    public Motorcycle() {
    }

    public Motorcycle(String brand, String model, int pricePerDayInSek, boolean rented) {
        this.brand = brand;
        this.model = model;
        this.pricePerDayInSek = pricePerDayInSek;
        this.rented = rented;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public int getPricePerDayInSek() {
        return pricePerDayInSek;
    }

    public void setPricePerDayInSek(int pricePerDayInSek) {
        this.pricePerDayInSek = pricePerDayInSek;
    }

    @Override
    public String toString() {
        return "Motorcycle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", pricePerDayInSek=" + pricePerDayInSek +
                ", isRented=" + rented +
                '}';
    }
}

