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
//    @Autowired
//    private CustomerServiceImpl cinemaCustomerService;

    @Autowired
    private MovieServiceImpl movieService;

    @Autowired
    private CinemaRoomServiceImpl cinemaRoomService;

    @Autowired
    private AddressServiceImpl CinemaAddressService;

    private CustomerService customerService;
    private AddressService addressService;
    private DishesService dishesService;
    private SushiRoomService sushiRoomService;
    private SushiBookingService bookingService;
    private OrderService orderService;

    @Autowired
    public AdminController(MovieService movieService, CinemaRoomService cinemaRoomService, CustomerService customerService, DishesService dishesService, SushiRoomService sushiRoomService, SushiBookingService sushiBookingService, OrderService orderService){
        this.movieService = movieService;
        this.cinemaRoomService = cinemaRoomService;
        this.customerService = customerService;
        this.dishesService = dishesService;
        this.sushiRoomService = sushiRoomService;
        this.bookingService = sushiBookingService;
        this.orderService = orderService;
    }

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        return customerService.findAllCustomers();
    }

    // Tobbe
    @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody Customer customer) {
        if (customer.getCustomerId() > 0){
            customer.setCustomerId(0);
        }
        return customerService.saveCustomer(customer);
    }


    //Tobbe
    @PutMapping("/customers/{id}")
    public Customer updateCustomer(@PathVariable int id, @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    //    @PutMapping("customers/{id}")
//    public Customer updateCustomer(@PathVariable int id, @RequestBody Customer s) {
//        logger.info("admin updated customer with ID " + id);
//        s.setId(id);
//        Customer customer = customerService.saveCustomer(s);
//        return customer;
//    }

    //Rickard
//    @PutMapping("/customers/{id}")
//    public ResponseEntity<String> updateCustomer(@RequestBody Customer updatedCustomer, @PathVariable int id) {
//        Customer existingCustomer = customerService.findCustomerById(id);
//        if (existingCustomer == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Kund med ID: " + id + " finns inte");
//        }
//
//        existingCustomer.setUsername(updatedCustomer.getUsername());
//        existingCustomer.setName(updatedCustomer.getName());
//
//        Address updatedAddress = updatedCustomer.getAddress();
//        Address existingAddress = existingCustomer.getAddress();
//
//        if (existingAddress != null && !existingAddress.equals(updatedAddress)) {
//            Address matchingAddress = addressService.findAddressByStreetAndPostalCodeAndCity(
//                    updatedAddress.getStreet(), updatedAddress.getPostalCode(), updatedAddress.getCity()
//            );
//
//            if (matchingAddress != null) {
//                existingCustomer.setAddress(matchingAddress);
//            } else {
//                existingAddress.setStreet(updatedAddress.getStreet());
//                existingAddress.setPostalCode(updatedAddress.getPostalCode());
//                existingAddress.setCity(updatedAddress.getCity());
//            }
//        } else if (existingAddress == null) {
//            Address newAddress = addressService.saveAddress(updatedAddress);
//            existingCustomer.setAddress(newAddress);
//        }
//
//        Customer updatedCustomerResult = customerService.saveCustomer(existingCustomer);
//
//        if (existingAddress != null) {
//            List<Customer> customersWithSameAddress = customerService.findCustomersByAddressId(existingAddress.getId());
//            if (customersWithSameAddress.size() == 0) {
//                addressService.deleteAddressById(existingAddress.getId());
//            }
//        }
//        logger.info("Admin updated customer with ID: " + id);
//        return ResponseEntity.ok("Kund med ID: " + id + " har uppdaterats.");
//    }

    // Tobbe
    @PostMapping("/customers")
    public Customer saveCustomer(@RequestBody Customer customer) {
        if (customer.getCustomerId() > 0){
            customer.setCustomerId(0);
        }
        return customerService.saveCustomer(customer);
    }



//    @DeleteMapping("/customers/{id}")
//    public String deleteCustomer(@PathVariable int id) {
//        logger.info("admin deleted customer with ID " + id);
//        customerService.deleteCustomerById(id);
//        return "kund med id " + id + " har raderats";
//    }



    //Tobbe
    @DeleteMapping("/customers/{id}")
    public String deleteCustomer(@PathVariable int id) {
        customerService.deleteCustomerById(id);
        return ("Customer with id: " + id + " has been deleted!");
    }

//    @PutMapping("customers/{id}")
//    public Customer updateCustomer(@PathVariable int id, @RequestBody Customer s) {
//        logger.info("admin updated customer with ID " + id);
//        s.setId(id);
//        Customer customer = customerService.saveCustomer(s);
//        return customer;
//    }


    @PostMapping("/movies")
    public Movie saveMovie(@RequestBody Movie movie) {
        logger.info("admin added movie " + movie.getTitle());
        movie.setId(0);
        return movieService.saveMovie(movie);
    }

    @DeleteMapping("/movies/{id}")
    public String deleteMovie(@PathVariable int id) {
        logger.info("admin deleted movie with ID " + id);
        movieService.deleteMovieById(id);
        return "film med id " + id + " har raderats";
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return movieService.findAllMovies();
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
        Dishes addedDish = dishesService.addDish(dish);
        logger.info("Admin added a new dish with ID: " + addedDish.getId());

        return dishesService.addDish(dish);
    }

    @DeleteMapping("/sushis/{id}")
    public String deleteDish(@PathVariable int id) {
        Dishes deletedDish = dishesService.findDishById(id);
        CurrencyConverter converter = new CurrencyConverter();
        if (deletedDish == null) {
            return "Maträtt med ID " + id + " hittades inte.";
        }

        List<OrderDetails> orderDetailsList = orderService.findOrdersContainingDish(deletedDish);

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

        List<BookingDetails> bookingDetailsList = bookingService.findBookingDetailsContainingDish(deletedDish);

        for (BookingDetails bookingDetails : bookingDetailsList) {
            int quantity = bookingDetails.getQuantity();
            double priceSEK = bookingDetails.getPriceSEK();

            SushiBooking booking = bookingDetails.getBooking();
            booking.setTotalPriceSEK(booking.getTotalPriceSEK() - priceSEK);
            try {
                booking.setTotalPriceYEN((int) converter.SekToRequestedCurrency(booking.getTotalPriceSEK(), "JPY"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        dishesService.deleteDish(id);
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
        List<Dishes> dishes = dishesService.findAllDishes();
        return dishes;
    }
}