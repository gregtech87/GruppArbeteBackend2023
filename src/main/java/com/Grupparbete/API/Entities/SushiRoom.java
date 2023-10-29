package com.Grupparbete.API.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "sushi_room")
public class SushiRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sushi_room_id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_guests", nullable = false)
    private int maxGuests;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "room")
    private List<SushiBooking> bookings;

    public SushiRoom() {
    }

    public SushiRoom(String name, int maxGuests, String description) {
        this.name = name;
        this.maxGuests = maxGuests;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SushiBooking> getBookings() {
        return bookings;
    }

    public void setBookings(List<SushiBooking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Rooms{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxGuests=" + maxGuests +
                ", equipment='" + description + '\'' +
                '}';
    }
}
