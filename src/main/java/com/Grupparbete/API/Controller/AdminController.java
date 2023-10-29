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
    public CinemaRoom updateRoom(@PathVariable int id, @RequestBody CinemaRoom s) {
        logger.info("admin updated room with ID " + id);
        s.setId(id);
        CinemaRoom updatedRoom = cinemaRoomService.saveRoom(s);
        return updatedRoom;
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

    @PutMapping("/rooms/{id}")
    public ResponseEntity<String> updateRoom(@RequestBody SushiRoom updatedRoom, @PathVariable int id) {
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

    @GetMapping("/sushis")
    public List<Dishes> findAllDishes() {
        List<Dishes> dishes = sushiDishesService.findAllDishes();
        return dishes;
    }
}