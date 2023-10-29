package com.Grupparbete.API.Controller;

import com.Grupparbete.API.CurrencyConverter;
import com.Grupparbete.API.DTO.BookingRequestDTO;
import com.Grupparbete.API.DTO.OrderItemDTO;
import com.Grupparbete.API.DTO.OrderRequestDTO;
import com.Grupparbete.API.DTO.ShowBookingDTO;
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
public class UserController {
    private static final Logger logger = LogManager.getLogger("MyLogger");

    private SushiBookingService sushiBookingService;
    private SushiOrderService sushiOrderService;
    private TripService tripService;
    private TripDestinationService tripDestinationService;
    private CinemaRoomService CinemaRoomService;

    private CinemaBookingService cinemaBookingService;
    private CinemaRoomService cinemaRoomService;



    @Autowired
    public UserController(SushiBookingService sushiBookingService, SushiOrderService sushiOrderService, TripService tripService, TripDestinationService tripDestinationService) {
        this.sushiBookingService = sushiBookingService;
        this.sushiOrderService = sushiOrderService;
        this.tripService = tripService;
        this.tripDestinationService = tripDestinationService;
    }


    @PostMapping("/ordersushis")
    public ResponseEntity<String> orderTakeaway(@RequestBody OrderRequestDTO orderRequestDTO) {
        int customerId = orderRequestDTO.getCustomerId();
        List<OrderItemDTO> orderItemDTOS = orderRequestDTO.getOrders();

        Order order = sushiOrderService.createTakeawayOrder(customerId, orderItemDTOS);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Något gick fel vid beställningen.");
        }
        logger.info("User added a new takeaway order with ID: " + order.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Beställning mottagen");
    }

    @PostMapping("/bookings")
    public ResponseEntity<String> reserveRoom(@RequestBody BookingRequestDTO bookingRequestDTO) {
        int customerId = bookingRequestDTO.getCustomerId();
        int roomId = bookingRequestDTO.getRoomId();
        int guests = bookingRequestDTO.getGuests();
        List<Integer> dishIds = bookingRequestDTO.getDishIds();
        List<Integer> quantities = bookingRequestDTO.getQuantities();

        try {
            SushiBooking sushiBooking = sushiBookingService.createReservation(customerId, roomId, guests, dishIds, quantities);

            if (sushiBooking == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Något gick fel vid bokningen.");
            }
            logger.info("User added a reservation with ID: " + sushiBooking.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Bokning mottagen");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/bookings/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable int id, @RequestBody BookingRequestDTO updatedBookingRequestDTO) {
        int room = updatedBookingRequestDTO.getRoomId();
        int guests = updatedBookingRequestDTO.getGuests();
        List<Integer> dishIds = updatedBookingRequestDTO.getDishIds();
        List<Integer> quantities = updatedBookingRequestDTO.getQuantities();

        try {
            SushiBooking updatedBooking = sushiBookingService.updateReservation(id, room, guests, dishIds, quantities);

            if (updatedBooking == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Något gick fel vid bokningen.");
            }
            logger.info("User updated Reservation with ID: " + id);

            return ResponseEntity.status(HttpStatus.CREATED).body("Bokning Uppdaterad");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<ShowBookingDTO> getBookingWithAllData(@PathVariable int id) {
        ShowBookingDTO showBookingDTO = sushiBookingService.getBookingWithDetails(id);

        if (showBookingDTO != null) {
            return ResponseEntity.ok(showBookingDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/trips")
    public List<Destination> findAllDestinations() {
        return tripDestinationService.findAll();
    }

    @GetMapping("/trips/{id}")
    public Destination getTrip(@PathVariable int id) {
        return tripDestinationService.findById(id);
    }

    @PostMapping("/trips")
    public Trip saveTrip(@RequestBody Trip trip) {
        if (trip.getTripId() > 0){
            trip.setTripId(0);
        }
        return tripService.save(trip);
    }

    @PutMapping("/trips/{id}")
    public Trip updateTrip(@PathVariable int id, @RequestBody Trip trip) {
        return tripService.update(id, trip);
    }


    @PostMapping("/bookings")
    public CinemaBooking saveBooking(@RequestBody CinemaBooking booking) {
        booking.setId(0);
        int totalPrice = cinemaBookingService.calculateTotalPrice(booking);
        booking.setTotalprice(totalPrice);
        try {
            booking.setTotalpriceusd((int) CurrencyConverter.SekToRequestedCurrency(booking.getTotalprice(), "USD"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        CinemaBooking savedBooking = cinemaBookingService.saveBooking(booking);
        logger.info("Customer saved a booking with id: " + savedBooking.getId());
        return savedBooking;
    }


    @PutMapping("bookings/{id}")
    public CinemaBooking updateBooking(@PathVariable int id, @RequestBody CinemaBooking s){
        logger.info("Customer updated booking with ID " + id);
        CinemaBooking booking = saveBooking(s);
        s.setId(id);
        return booking;
    }
    @GetMapping("/bookings/{id}")
    public Optional<CinemaBooking> getBooking(@PathVariable int id) {
        return cinemaBookingService.findById(id);
    }
}

