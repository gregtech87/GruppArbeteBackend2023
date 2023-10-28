package com.Grupparbete.API.DAO;

import com.Grupparbete.API.Entities.BookingDetails;
import com.Grupparbete.API.Entities.Dishes;
import com.Grupparbete.API.Entities.SushiBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SushiBookingRepository extends JpaRepository<SushiBooking, Integer> {
    @Query("SELECT bd FROM BookingDetails bd WHERE bd.dish = :dish")
    List<BookingDetails> findBookingDetailsContainingDish(@Param("dish") Dishes dish);
}
