package com.Grupparbete.API.Service;

import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.McBooking;
import com.Grupparbete.API.Entities.Motorcycle;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface McCustomerServiceRepository {
    List<Motorcycle> findAllAvailableMcs();
    List<McBooking> findAllBookingsByBikeId(int id, LocalDate tomorrow);
    McBooking save(McBooking mcBooking);
    Optional<McBooking> findBookingById(int id);
    List<McBooking> findAllBookings();
    Optional<Customer> findCustomerByEmail(String email);
    Motorcycle findMotorcycleById(int id);

    void saveMc(Motorcycle mc);
}
