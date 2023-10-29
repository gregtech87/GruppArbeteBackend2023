package com.Grupparbete.API.Entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "booking_details")
public class BookingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    private SushiBooking booking;

    @Column(name = "guests")
    private int guests;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private SushiRoom room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id")
    private Dishes dish;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "priceSEK", precision = 10, scale = 2)
    private double priceSEK;

    @Column(name = "priceYEN")
    private int priceYEN;

    @Column(name = "booking_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;

    public BookingDetails() {
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

    public SushiBooking getBooking() {
        return booking;
    }

    public void setBooking(SushiBooking booking) {
        this.booking = booking;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public SushiRoom getRoom() {
        return room;
    }

    public void setRoom(SushiRoom room) {
        this.room = room;
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

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "BookingDetails{" +
                "id=" + id +
                ", customer=" + customer +
                ", booking=" + booking +
                ", guests=" + guests +
                ", room=" + room +
                ", dish=" + dish +
                ", quantity=" + quantity +
                ", priceSEK=" + priceSEK +
                ", priceYEN=" + priceYEN +
                ", bookingDate=" + bookingDate +
                '}';
    }
}
