package com.Grupparbete.API.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_trips")
public class CustomerTripJoinTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "trip_id")
        private int tripId;

    public CustomerTripJoinTable() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "CustomTripJoinTable{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", tripId=" + tripId +
                '}';
    }
}
