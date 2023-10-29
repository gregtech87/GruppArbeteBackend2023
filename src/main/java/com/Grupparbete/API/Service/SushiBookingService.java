package com.Grupparbete.API.Service;

import com.Grupparbete.API.DTO.ShowBookingDTO;
import com.Grupparbete.API.Entities.SushiBookingDetails;
import com.Grupparbete.API.Entities.Dishes;
import com.Grupparbete.API.Entities.SushiBooking;

import java.util.List;

public interface SushiBookingService {
    void deleteBooking(int id);
    List<SushiBooking> findAllBookings();
    SushiBooking findBookingById(int id);
    ShowBookingDTO getBookingWithDetails(int bookingId);
    SushiBooking updateReservation(int bookingId,int roomId, int guests, List<Integer> dishIds, List<Integer> quantities);
    SushiBooking createReservation(int customerId, int roomId, int guests, List<Integer> dishIds, List<Integer> quantities);
    SushiBooking saveBooking(SushiBooking booking);
    List<SushiBookingDetails> findBookingDetailsContainingDish(Dishes dish);

}

