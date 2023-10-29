package com.Grupparbete.API.Service;

import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.McBooking;
import com.Grupparbete.API.Entities.Motorcycle;

import java.util.List;
import java.util.Optional;

public interface McAdminServiceRepository {
    List<Customer> getAllCustomers();
    Customer save(Customer customer);
    Optional<Customer> findCustomerById(int id);
    String deleteCustomerById(int id);
    Motorcycle save(Motorcycle motorcycle);
    List<Motorcycle> getAllBikes();
    Optional<Motorcycle> findBikeById(int id);
    Optional<McBooking> findBookingById(int id);
    String deleteBookingById(int id);
    McBooking findById(int id);
}
