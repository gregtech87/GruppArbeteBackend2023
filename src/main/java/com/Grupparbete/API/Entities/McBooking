package com.Grupparbete.API.Entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name="mcbooking")
public class McBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name="end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(name="total_price_in_sek")
    private int totalPriceInSEK;

    @Column(name="total_price_in_gbp")
    private double totalPriceInGBP;


    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name="customer")
    private Customer customer;

    @OneToOne
    @JoinColumn(name ="mc")
    private Motorcycle mc;

    public McBooking() {
    }

    public McBooking(LocalDate startDate, LocalDate endDate, int totalPriceInSEK, Customer customer, Motorcycle mc) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPriceInSEK = totalPriceInSEK;
        this.customer = customer;
        this.mc = mc;
    }

    public McBooking(LocalDate startDate, LocalDate endDate, int totalPriceInSEK, double totalPriceInGBP, Customer customer, Motorcycle mc) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPriceInSEK = totalPriceInSEK;
        this.totalPriceInGBP = totalPriceInGBP;
        this.customer = customer;
        this.mc = mc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getTotalPriceInSEK() {
        return totalPriceInSEK;
    }

    public void setTotalPriceInSEK(int totalPriceInSEK) {
        this.totalPriceInSEK = totalPriceInSEK;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Motorcycle getMc() {
        return mc;
    }

    public void setMc(Motorcycle mc) {
        this.mc = mc;
    }

    public double getTotalPriceInGBP() {
        return totalPriceInGBP;
    }

    public void setTotalPriceInGBP(double totalPriceInGBP) {
        this.totalPriceInGBP = totalPriceInGBP;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalPriceInSEK=" + totalPriceInSEK +
                ", totalPriceInGBP=" + totalPriceInGBP +
                ", customer=" + customer +
                ", mc=" + mc +
                '}';
    }
}
