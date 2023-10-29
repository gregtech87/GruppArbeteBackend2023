package com.Grupparbete.API.DAO;

import com.Grupparbete.API.Entities.McBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface McBookingRepository extends JpaRepository<McBooking, Integer> {

    List<McBooking> findBookingsByMc_IdAndStartDateIsBefore(int id, LocalDate tomorrow);
    McBooking save(McBooking mcBooking);
    Optional<McBooking> findById(int id);
    List<McBooking> findAll();
    McBooking findBookingById(int id);
}
