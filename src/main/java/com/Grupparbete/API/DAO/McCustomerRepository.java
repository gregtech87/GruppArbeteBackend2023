package com.Grupparbete.API.DAO;

import com.Grupparbete.API.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface McCustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByEmail(String email);
    String deleteById(int id);
}
