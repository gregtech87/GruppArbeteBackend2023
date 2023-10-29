package com.Grupparbete.API.Controller;

import com.Grupparbete.API.CurrencyConverter;
import com.Grupparbete.API.Entities.Address;
import com.Grupparbete.API.Entities.Customer;
import com.Grupparbete.API.Entities.McBooking;
import com.Grupparbete.API.Entities.Motorcycle;
import com.Grupparbete.API.Service.McCustomerServiceRepository;
import com.Grupparbete.API.exception.McBookingNotFoundException;
import com.Grupparbete.API.exception.McCustomerNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class McCustomerController {

    private McCustomerServiceRepository mcCustomerService;
    private static final Logger logger = LogManager.getLogger("myLogger");

    @Autowired
    public void CustomerController(McCustomerServiceRepository mcCustomerService) {
        this.mcCustomerService = mcCustomerService;
    }
    @GetMapping("/available_bikes")
    public List<Motorcycle> getAllAvailableBikes(){
        return mcCustomerService.findAllAvailableMcs();
    }

    @PostMapping("/rent_bike")
    public McBooking rentBike(@RequestBody McBooking mcBooking){

        Optional<Customer> customerOptional = mcCustomerService.findCustomerByEmail(mcBooking.getCustomer().getEmail());

        if(customerOptional.isEmpty()){
            logger.error("Mailaddressen: "+ mcBooking.getCustomer().getEmail() + " finns inte i vårt system, vänligen kolla så att den är skriven rätt");
            throw new McCustomerNotFoundException("Mailaddressen finns inte i vårt system, vänligen kolla så du har angivit rätt mailadress");
        }

        Customer customer = customerOptional.get();
        Address address = customer.getAddress();

        customer.setAddress(address);

        Motorcycle mc = mcCustomerService.findMotorcycleById(mcBooking.getMc().getId());

        mc.setRented(true);

        LocalDate sd = mcBooking.getStartDate();
        LocalDate ed = mcBooking.getEndDate();

        int totalPrice = calculateTotalPrice(sd, ed, mc.getPricePerDayInSek());

        CurrencyConverter cc = new CurrencyConverter();
        double priceInGbp = 0.0;

        try {
            priceInGbp = cc.SekToRequestedCurrency(totalPrice, "GBP");
        }
        catch (IOException e){
            logger.warn("Fel valuta skrevs in, försök igen.");
            e.getMessage();
        }
        McBooking b = new McBooking(sd, ed, totalPrice, priceInGbp, customer, mc);
        mcCustomerService.save(b);
        logger.info("Kund: "+ customer.getFirstName()+ " " + customer.getLastName() +" bokade mc: " + mc.getBrand() +" " + mc.getModel() + ". Bokningsnummer: " + b.getId());
        return b;

    }


    @PutMapping("/booking/{id}")
    public McBooking updateBooking(@PathVariable int id, @RequestBody McBooking mcBooking){

        Optional<McBooking> tempBooking = mcCustomerService.findBookingById(id);
        System.out.println(tempBooking.toString());

        if(tempBooking.isEmpty()){
            logger.error("Kund försökte uppdatera bokning med id: " + id + " men det gick inte, då id:et inte finns i databsen");
            throw new McBookingNotFoundException("Bokningsnumret finns inte i databsen");
        }

        McBooking b = tempBooking.get();
        b.toString();

        Customer customer = tempBooking.get().getCustomer();
        Customer optionalCustomer = mcCustomerService.findCustomerByEmail(mcBooking.getCustomer().getEmail()).get();

        if(!(customer.equals(optionalCustomer))){
            b.setCustomer(optionalCustomer);
            logger.info("Ny kund i bokning. Från kund: " + customer.getEmail() + " till kund: " + optionalCustomer.getEmail());
        }

        Motorcycle mc = tempBooking.get().getMc();
        Motorcycle optionalMc = mcCustomerService.findMotorcycleById(mcBooking.getMc().getId());

        if(!(mc.equals(optionalMc))){

            optionalMc.setRented(true);
            b.setMc(optionalMc);

            mc.setRented(false);
            mcCustomerService.saveMc(mc);

            logger.info("Kund bytte ut mc i sin bokningfrån " + mc.getModel()+ ", "+mc.getBrand() + " to: " + optionalMc.getModel() + ", " + optionalMc.getBrand());
        }
        int newTotalPrice = calculateTotalPrice(mcBooking.getStartDate(), mcBooking.getEndDate(), b.getMc().getPricePerDayInSek());
        double newTotalPriceInGbp = 0.0;

        try{
            CurrencyConverter cc = new CurrencyConverter();
            newTotalPriceInGbp = cc.SekToRequestedCurrency(newTotalPrice, "GBP");
        } catch (IOException e){

        }

        b.setTotalPriceInSEK(newTotalPrice);
        b.setTotalPriceInGBP(newTotalPriceInGbp);
        logger.info("Kund uppdaterar bokning: " + b.getId());
        return mcCustomerService.save(b);
    }

    @GetMapping("/bikes/{id}")
    public List<McBooking> getCurrentAndPreviousBookings(@PathVariable int id){

        return mcCustomerService.findAllBookingsByBikeId(id, LocalDate.from(LocalDateTime.now()).plusDays(1));
    }

    private int calculateTotalPrice(LocalDate sd, LocalDate ed, int pricePerDay){
        return (int) ChronoUnit.DAYS.between(sd, ed) * pricePerDay;
    }
}
