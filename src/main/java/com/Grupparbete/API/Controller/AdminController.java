package com.Grupparbete.API.Controller;

import com.Grupparbete.API.CurrencyConverter;
import com.Grupparbete.API.Entities.*;
import com.Grupparbete.API.Service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AdminController {

    private static final Logger logger = LogManager.getLogger("MyLogger");

    private CinemaMovieService cinemaMovieService;
    private CinemaRoomService cinemaRoomService;
    private CustomerService customerService;
    private SushiDishesService sushiDishesService;
    private SushiRoomService sushiRoomService;
    private SushiBookingService bookingService;
    private SushiOrderService sushiOrderService;

    @Autowired
    public AdminController(CinemaMovieService cinemaMovieService, CinemaRoomService cinemaRoomService, CustomerService customerService, SushiDishesService sushiDishesService, SushiRoomService sushiRoomService, SushiBookingService sushiBookingService, SushiOrderService sushiOrderService){
        this.cinemaMovieService = cinemaMovieService;
        this.cinemaRoomService = cinemaRoomService;
        this.customerService = customerService;
        this.sushiDishesService = sushiDishesService;
        this.sushiRoomService = sushiRoomService;
        this.bookingService = sushiBookingService;
        this.sushiOrderService = sushiOrderService;
    }

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        return customerService.findAllCustomers();
    }

    @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody Customer customer) {
        if (customer.getCustomerId() > 0){
            customer.setCustomerId(0);
        }
        return customerService.saveCustomer(customer);
    }

    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable int id, @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/customers/{id}")
    public String deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomerById(id);
        return ("Customer with id: " + id + " has been deleted!");
    }

    @PostMapping("/movies")
    public Movie saveMovie(@RequestBody Movie movie) {
        logger.info("admin added movie " + movie.getTitle());
        movie.setId(0);
        return cinemaMovieService.saveMovie(movie);
    }

    @DeleteMapping("/movies/{id}")
    public String deleteMovie(@PathVariable int id) {
        logger.info("admin deleted movie with ID " + id);
        cinemaMovieService.deleteMovieById(id);
        return "film med id " + id + " har raderats";
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return cinemaMovieService.findAllMovies();
    }

    @PutMapping("rooms/{id}")
    public CinemaRoom updateCinemaRoom(@PathVariable int id, @RequestBody CinemaRoom s) {
        logger.info("admin updated room with ID " + id);
        s.setId(id);
        CinemaRoom updatedRoom = cinemaRoomService.saveRoom(s);
        return updatedRoom;
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<String> updateSushiRoom(@RequestBody SushiRoom updatedRoom, @PathVariable int id) {
        SushiRoom existingRoom = sushiRoomService.findRoomById(id);
        if (existingRoom == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Rum med ID: " + id + " finns inte");
        }

        existingRoom.setName(updatedRoom.getName());
        existingRoom.setDescription(updatedRoom.getDescription());
        existingRoom.setMaxGuests(updatedRoom.getMaxGuests());

        SushiRoom updated = sushiRoomService.updateRoom(existingRoom, id);

        if (updated != null) {
            logger.info("Admin updated room with ID: " + id);
            return ResponseEntity.ok("Rum med ID " + id + " har uppdaterats.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Misslyckades med att uppdatera rummet.");
        }
    }

    @PostMapping("/sushis")
    public Dishes addDish(@RequestBody Dishes dish) {
        Dishes addedDish = sushiDishesService.addDish(dish);
        logger.info("Admin added a new dish with ID: " + addedDish.getId());

        return sushiDishesService.addDish(dish);
    }

    @DeleteMapping("/sushis/{id}")
    public String deleteDish(@PathVariable int id) {
        Dishes deletedDish = sushiDishesService.findDishById(id);
        CurrencyConverter converter = new CurrencyConverter();
        if (deletedDish == null) {
            return "Maträtt med ID " + id + " hittades inte.";
        }

        List<OrderDetails> orderDetailsList = sushiOrderService.findOrdersContainingDish(deletedDish);

        for (OrderDetails orderDetails : orderDetailsList) {
            int quantity = orderDetails.getQuantity();
            double priceSEK = orderDetails.getPriceSEK();

            Order order = orderDetails.getOrder();
            order.setQuantity(order.getQuantity() - quantity);
            order.setTotalPriceSEK(order.getTotalPriceSEK() - priceSEK);
            try {
                order.setTotalPriceYEN((int) converter.SekToRequestedCurrency(order.getTotalPriceSEK(), "JPY"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<SushiBookingDetails> sushiBookingDetailsList = bookingService.findBookingDetailsContainingDish(deletedDish);

        for (SushiBookingDetails sushiBookingDetails : sushiBookingDetailsList) {
            int quantity = sushiBookingDetails.getQuantity();
            double priceSEK = sushiBookingDetails.getPriceSEK();

            SushiBooking booking = sushiBookingDetails.getBooking();
            booking.setTotalPriceSEK(booking.getTotalPriceSEK() - priceSEK);
            try {
                booking.setTotalPriceYEN((int) converter.SekToRequestedCurrency(booking.getTotalPriceSEK(), "JPY"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        sushiDishesService.deleteDish(id);
        logger.info("Admin deleted Dish with ID: " + id);
        return "Maträtt med ID " + id + " har tagits bort.";
    }



    @GetMapping("/sushis")
    public List<Dishes> findAllDishes() {
        List<Dishes> dishes = sushiDishesService.findAllDishes();
        return dishes;
    }
    //sebbe
    @GetMapping("/allBikes")
    public List<Motorcycle> getAllBikes(){
        return mcAdminServiceRepository.getAllBikes();
    }

    //sebbe
    @PutMapping("/bike/{id}")
    public Motorcycle updateMC(@RequestBody Motorcycle mc, @PathVariable int id){

        Optional<Motorcycle> bike = mcAdminServiceRepository.findBikeById(id);

        if(!bike.isPresent()){
            logger.error("Mc med id: " + id + " finns inte i databasen.");
            throw new MotorcycleNotFoundException("Bike with ID is not found");
        }

        mc.setId(id);
        logger.info("Admin uppdaterade mc: " + mc.getId() + " " + mc.getBrand() +" " + mc.getModel());
        return mcAdminServiceRepository.save(mc);
    }

    //sebbe
    @DeleteMapping("/bookings/{id}")
    public String deleteBooking(@PathVariable int id){
        Optional<McBooking> booking  = mcAdminServiceRepository.findBookingById(id);

        if(!booking.isPresent()){
            logger.error("Ingen bokning med id: " + id + " hittades i vårat system");
            throw new McBookingNotFoundException("No Booking with this reference was found, please check again.");
        }

        McBooking b = mcAdminServiceRepository.findById(id);
        Motorcycle mc = b.getMc();
        mc.setRented(false);
        mcAdminServiceRepository.deleteBookingById(id);

        logger.info("Admin tog bort bokning med nummer: " + b.getId());
        return "Bokningen har tagits bort.";
    }

    //sebbe
    @PostMapping("/bike")
    public Motorcycle addMC(@RequestBody Motorcycle mc){

        mc.setId(0);
        logger.info("Admin la till en ny MC: " + mc.getBrand() + " " + mc.getModel());
        return mcAdminServiceRepository.save(mc);
    }

    //sebbe
    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable int id, @RequestBody Customer customer){
        Optional<Customer> c = mcAdminServiceRepository.findCustomerById(id);
        if(!c.isPresent()){
            logger.error("Kund med id: " + id + " finns inte i databasen.");
            throw new McCustomerNotFoundException("We cant find a customer with this id in our database");
        }
        customer.setCustomerId(id);
        logger.info("Admin uppdaterade kund: " + customer.getCustomerId() +", "  + customer.getFirstName() + " " + customer.getLastName());
        return mcAdminServiceRepository.save(customer);
    }

    //sebbe
    @DeleteMapping("/mccustomers/{id}")
    public String deleteMcCustomer(@PathVariable int id){
        Optional<Customer> customer = mcAdminServiceRepository.findCustomerById(id);
        if(customer.isEmpty()){
            logger.error("Kund med id: " + id + " finns inte i databasen.");
            throw new McCustomerNotFoundException("Customer with the id is not found in the database");
        }
        logger.info("Admin tog bort kund med id: " + id);
        return mcAdminServiceRepository.deleteCustomerById(id);
    }
    //sebbe
    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer){

        customer.setCustomerId(0);
        logger.info("Admin la till ny kund: " + customer.toString());
        return mcAdminServiceRepository.save(customer);
    }
    //sebbe
    @GetMapping("/customers")
    public List<Customer> getAllCustomers(){
        return mcAdminServiceRepository.getAllCustomers();
    }


}
