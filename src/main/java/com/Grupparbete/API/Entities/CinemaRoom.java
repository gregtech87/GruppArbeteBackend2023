package com.Grupparbete.API.Entities;


import jakarta.persistence.*;

@Entity
@Table(name = "cinema_room")
public class CinemaRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_room_id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "tech")
    private String tech;
    @Column(name = "maxGuests")
    private int maxGuests;
    @Column(name = "price")
    private int price;

    public CinemaRoom() {
    }

    public CinemaRoom(int id, String title, String tech, int maxGuests, int price) {
        this.id = id;
        this.title = title;
        this.tech = tech;
        this.maxGuests = maxGuests;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
