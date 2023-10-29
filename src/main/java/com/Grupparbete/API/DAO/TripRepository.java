package com.Grupparbete.API.DAO;


import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    @Query("SELECT t FROM Customer c JOIN c.trips t WHERE c.customerId = :customerId AND t.tripId = :departureDate")
    Trip findTripByCustomerIdAndDepartureDate(@Param("departureDate") int tripId, @Param("customerId") int customerId);


    @Query("SELECT t FROM Customer c JOIN c.trips t WHERE c.customerId = :customerId")
    List<Trip> findTripsByCustomerId(@Param("customerId") int customerId);

    @Query("SELECT c FROM Customer c JOIN c.trips t WHERE t.tripId = :tripId")
    Customer findCustomerByTripId(@Param("tripId")int tripId);
}
