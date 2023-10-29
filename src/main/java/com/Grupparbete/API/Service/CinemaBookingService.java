package com.Grupparbete.API.Service;

import com.Grupparbete.API.Entities.CinemaBooking;

import java.util.Optional;

public interface CinemaBookingService {

    CinemaBooking saveBooking(CinemaBooking booking);
    Optional<CinemaBooking> findById(int id);
      int calculateTotalPrice(CinemaBooking booking);
}
