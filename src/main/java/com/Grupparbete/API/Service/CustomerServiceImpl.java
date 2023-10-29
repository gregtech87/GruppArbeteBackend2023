package com.Grupparbete.API.Service;

import com.Grupparbete.API.DAO.CustomerRepository;
import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.Trip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LogManager.getLogger("MyLogger");
    private CustomerRepository customerRepository;
    private TripService tripService;
    private AddressService addressService;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, TripService tripService, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.tripService = tripService;
        this.addressService = addressService;
    }
    @Override
    public List<Customer> findAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();
        for (Customer c : customerList){
            for (Trip t: c.getTrips()) {
                try {
                    t = tripService.makeSureCurrencyIsUpdated(t, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return customerList;
    }

    @Override
    public Customer findCustomerById(int id) {
        Optional<Customer> c = customerRepository.findById(id);
        Customer customer = new Customer();
        if (c.isPresent()){
            customer = c.get();
            for (Trip t: customer.getTrips()){
                try {
                    t = tripService.makeSureCurrencyIsUpdated(t, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return customer;
    }

    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        customer.setAddress(addressService.checkIfExistsInDatabaseIfNotSave(customer.getAddress(), true));
        customer.setTrips(tripService.inspectTripList(customer.getTrips(), customer.getCustomerId()));
        logger.info("Customer saved: " + customer);
        return customerRepository.save(customer);
    }

//    @Override
//    public Customer updateCustomer(Customer customers, int id) {
//        Customer c = customerRepository.findById(id).orElseThrow(()-> new RuntimeException("Kunde inte finna medlem "+ customers +" med Id: "+id));
//        c.setUsername(customers.getUsername());
//        c.setName(customers.getName());
//        c.setAddress(customers.getAddress());
//        customerRepository.save(c);
//        return c;
//    }

    @Override
    public Customer updateCustomer(int id, Customer customer) {
        Customer customerFromDb = findCustomerById(id);
        customerFromDb.setUserName(customer.getUserName());
        customerFromDb.setPassword(customer.getPassword());
        customerFromDb.setFirstName(customer.getFirstName());
        customerFromDb.setLastName(customer.getLastName());
        customerFromDb.setAddress(addressService.checkIfExistsInDatabaseIfNotSave(customer.getAddress(), true));
        customerFromDb.setTrips(tripService.inspectTripList(customer.getTrips(),id));
        customerFromDb.setEmail(customer.getEmail());
        customerFromDb.setPhone(customer.getPhone());
        customerFromDb.setDateOfBirth(customer.getDateOfBirth());
        customer.setCustomerId(id);
        logger.info("Customer edited \nFrom: " + customer + "\nTo: " + customerFromDb);
        return saveCustomer(customerFromDb);
    }


 @Override
 @Transactional
 public void deleteCustomerById(int id) {
     Customer customer = findCustomerById(id);
     List<Trip> tripList = customer.getTrips();
     List<Trip> tripListForRemoval = new ArrayList<>();
     for (int i = tripList.size()-1; i >= 0; i--){
         tripListForRemoval.add(tripList.get(i));
         tripList.remove(i);
     }
     saveCustomer(customer);
     for (Trip t : tripListForRemoval){
         tripService.deleteById(t.getTripId());
     }

     int addressId = customer.getAddress().getId();

     customerRepository.deleteById(id);
     logger.info("Customer deleted: " + customer);

     List<Customer> remainingCustomersWithSameAddress = findCustomersByAddressId(addressId);
     if (remainingCustomersWithSameAddress.isEmpty()) {
         addressService.deleteAddressById(addressId);
     }
 }
    @Override
    public List<Customer> findCustomersByAddressId(int addressId) {
        return customerRepository.findByAddress_Id(addressId);
    }

}