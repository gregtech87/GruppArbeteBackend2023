package com.Grupparbete.API.DAO;

import com.Grupparbete.API.Entities.SushiBookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDetailsRepository extends JpaRepository<SushiBookingDetails, Integer> {
}
