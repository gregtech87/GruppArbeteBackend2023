package com.Grupparbete.API.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Sushibooking")
public class SushiBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "num_guests")
    private int guests;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sushi_room_id")
    private SushiRoom room;

    @Column(name = "booking_date")
    private Date bookingDate = new Date();

    @Column(name = "total_price_sek")
    private double totalPriceSEK;

    @Column(name = "total_price_yen")
    private int totalPriceYEN;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonIgnore
    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SushiBookingDetails> sushiBookingDetails;

    public SushiBooking() {
    }

    public SushiBooking(Customer customer, int guests, SushiRoom room, double totalSEKPrice, Date bookingDate) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getTotalPriceSEK() {
        return totalPriceSEK;
    }

    public void setTotalPriceSEK(double totalPriceSEK) {
        this.totalPriceSEK = totalPriceSEK;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getTotalPriceYEN() {
        return totalPriceYEN;
    }

    public void setTotalPriceYEN(int totalPriceYEN) {
        this.totalPriceYEN = totalPriceYEN;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SushiBookingDetails> getBookingDetails() {
        return sushiBookingDetails;
    }

    public void setBookingDetails(List<SushiBookingDetails> sushiBookingDetails) {
        this.sushiBookingDetails = sushiBookingDetails;
    }

    @Override
    public String toString() {
        return "Bookings{" +
                "id=" + id +
                ", guests=" + guests +
                ", room=" + room +
                ", bookingDate=" + bookingDate +
                ", totalPriceSEK=" + totalPriceSEK +
                ", totalPriceYEN=" + totalPriceYEN +
                ", customer=" + customer +
                ", bookingDetails=" + sushiBookingDetails +
                '}';
    }
}
