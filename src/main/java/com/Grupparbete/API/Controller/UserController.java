package com.Grupparbete.API.Controller;

import com.Grupparbete.API.DTO.BookingRequestDTO;
import com.Grupparbete.API.DTO.OrderItemDTO;
import com.Grupparbete.API.DTO.OrderRequestDTO;
import com.Grupparbete.API.DTO.ShowBookingDTO;
import com.Grupparbete.API.Entities.Destination;
import com.Grupparbete.API.Entities.Order;
import com.Grupparbete.API.Entities.SushiBooking;
import com.Grupparbete.API.Entities.Trip;
import com.Grupparbete.API.Service.DestinationService;
import com.Grupparbete.API.Service.OrderService;
import com.Grupparbete.API.Service.SushiBookingService;
import com.Grupparbete.API.Service.TripService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private static final Logger logger = LogManager.getLogger("MyLogger");

    private SushiBookingService sushiBookingService;
    private OrderService orderService;
    private TripService tripService;
    private DestinationService destinationService;



    @Autowired
    public UserController(SushiBookingService sushiBookingService, OrderService orderService, TripService tripService, DestinationService destinationService) {
        this.sushiBookingService = sushiBookingService;
        this.orderService = orderService;
        this.tripService = tripService;
        this.destinationService = destinationService;
    }


    @PostMapping("/ordersushis")
    public ResponseEntity<String> orderTakeaway(@RequestBody OrderRequestDTO orderRequestDTO) {
        int customerId = orderRequestDTO.getCustomerId();
        List<OrderItemDTO> orderItemDTOS = orderRequestDTO.getOrders();

        Order order = orderService.createTakeawayOrder(customerId, orderItemDTOS);

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
        return destinationService.findAll();
    }

    @GetMapping("/trips/{id}")
    public Destination getTrip(@PathVariable int id) {
        return destinationService.findById(id);
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
}
