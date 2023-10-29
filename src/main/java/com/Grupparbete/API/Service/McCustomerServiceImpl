package com.Grupparbete.API.Service;

import com.Grupparbete.API.DAO.McBookingRepository;
import com.Grupparbete.API.DAO.McCustomerRepository;
import com.Grupparbete.API.DAO.MotorcycleRepository;
import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.McBooking;
import com.Grupparbete.API.Entities.Motorcycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class McCustomerServiceImpl implements McCustomerServiceRepository {

    private MotorcycleRepository motorcycleRepository;
    private McBookingRepository mcBookingRepository;
    private McCustomerRepository mcCustomerRepository;

    @Autowired
    public McCustomerServiceImpl (MotorcycleRepository mcr, McBookingRepository mcBookingRepository, McCustomerRepository mcCustomerRepository){
        this.motorcycleRepository = mcr;
        this.mcBookingRepository = mcBookingRepository;
        this.mcCustomerRepository = mcCustomerRepository;
    }

    @Override
    public List<Motorcycle> findAllAvailableMcs() {
        return motorcycleRepository.findMotorcyclesByRentedIsFalse();

    }

    @Override
    public List<McBooking> findAllBookingsByBikeId(int id, LocalDate tomorrow) {
        List<McBooking> list = mcBookingRepository.findBookingsByMc_IdAndStartDateIsBefore(id, tomorrow);
        return list;
    }

    @Override
    public McBooking save(McBooking mcBooking){
        return mcBookingRepository.save(mcBooking);
    }

    @Override
    public Optional<McBooking> findBookingById(int id){
        return mcBookingRepository.findById(id);
    }

    @Override
    public List<McBooking> findAllBookings(){
        return mcBookingRepository.findAll();
    }


    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return mcCustomerRepository.findCustomerByEmail(email);
    }

    @Override
    public Motorcycle findMotorcycleById(int id) {
        return motorcycleRepository.findMotorcycleById(id);
    }

    @Override
    public void saveMc(Motorcycle mc) {
        motorcycleRepository.save(mc);
    }
}
