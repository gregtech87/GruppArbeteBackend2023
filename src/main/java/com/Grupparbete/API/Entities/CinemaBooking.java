package com.Grupparbete.API.Entities;

import jakarta.persistence.*;


@Entity
@Table(name = "booking")
public class CinemaBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "guests")
    private int guests;
    @Column(name = "totalprice")
    private int totalprice;
    @Column(name = "totalpriceusd")
    private int totalpriceusd;
    @ManyToOne
    @JoinColumn(name = "room")
    private CinemaRoom room;

    @ManyToOne
    @JoinColumn(name = "movie")
    private Movie movie;

    public CinemaBooking() {
    }

    public CinemaBooking(int guests,CinemaRoom room, Movie movie,int totalprice,int totalpriceusd) {
        this.guests = guests;
        this.room = room;
        this.movie = movie;
        this.totalprice = totalprice;
        this.totalpriceusd = totalpriceusd;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public int getGuests() {
        return guests;
    }

    public int getTotalpriceusd() {
        return totalpriceusd;
    }

    public void setTotalpriceusd(int totalpriceusd) {
        this.totalpriceusd = totalpriceusd;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CinemaRoom getRoom() {
        return room;
    }

    public void setRoom(CinemaRoom room) {
        this.room = room;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

}
