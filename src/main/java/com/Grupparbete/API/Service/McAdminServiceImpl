package com.Grupparbete.API.Service;

import com.Grupparbete.API.DAO.McBookingRepository;
import com.Grupparbete.API.DAO.McCustomerRepository;
import com.Grupparbete.API.DAO.MotorcycleRepository;
import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.McBooking;
import com.Grupparbete.API.Entities.Motorcycle;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class McAdminServiceImpl implements McAdminServiceRepository{

    private McCustomerRepository customerRepository;
    private MotorcycleRepository motorcycleRepository;
    private McBookingRepository bookingRepository;

    @Autowired
    public McAdminServiceImpl(McCustomerRepository customerRepository, MotorcycleRepository motorcycleRepository, McBookingRepository bookingRepository) {
        this.customerRepository = customerRepository;
        this.motorcycleRepository = motorcycleRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findCustomerById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional
    public String deleteCustomerById(int id) {
        customerRepository.deleteById(id);
        return "Customer has been deleted from the databse";
    }

    @Override
    @Transactional
    public Motorcycle save(Motorcycle mc){
        return motorcycleRepository.save(mc);
    }

    @Override
    public List<Motorcycle> getAllBikes() {
        return motorcycleRepository.findAll();
    }

    @Override
    public Optional<Motorcycle> findBikeById(int id) {
        return motorcycleRepository.findById(id);
    }

    @Override
    public Optional<McBooking> findBookingById(int id) {
        return bookingRepository.findById(id);
    }

    @Override
    public String deleteBookingById(int id) {
        bookingRepository.deleteById(id);
        return "Reservationen har tagits bort";
    }

    @Override
    public McBooking findById(int id) {
        return bookingRepository.findBookingById(id);
    }
}
