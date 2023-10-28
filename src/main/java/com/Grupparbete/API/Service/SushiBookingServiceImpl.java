package com.Grupparbete.API.Service;

import com.Grupparbete.API.CurrencyConverter;
import com.Grupparbete.API.DAO.BookingDetailsRepository;
import com.Grupparbete.API.DTO.OrderItemDTO;
import com.Grupparbete.API.DTO.ShowBookingDTO;
import com.Grupparbete.API.Entities.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Grupparbete.API.DAO.SushiBookingRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SushiBookingServiceImpl implements SushiBookingService {

    private SushiBookingRepository bookingRepository;
    private CustomerService customerService;
    private DishesService dishesService;
    private SushiRoomService roomService;
    private BookingDetailsRepository bookingDetailsRepository;


    @Autowired
    public SushiBookingServiceImpl(SushiBookingRepository bookingRepository, CustomerService custService, DishesService dishService, SushiRoomService roomService, BookingDetailsRepository bookDetailsRepository) {
        this.bookingRepository = bookingRepository;
        customerService = custService;
        dishesService = dishService;
        roomService = roomService;
        bookingDetailsRepository = bookDetailsRepository;
    }


    @Override
    public void deleteBooking(int id) {
        bookingRepository.deleteById(id);
    }


    @Override
    public List<SushiBooking> findAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public SushiBooking findBookingById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public ShowBookingDTO getBookingWithDetails(int bookingId) {
        Optional<SushiBooking> bookingOptional = bookingRepository.findById(bookingId);

        if (bookingOptional.isEmpty()) {
            throw new IllegalArgumentException("Bokningen finns inte.");
        }

        SushiBooking booking = bookingOptional.get();

        Customer customer = booking.getCustomer();
        SushiRoom room = booking.getRoom();
        List<BookingDetails> bookingDetails = booking.getBookingDetails();

        List<OrderItemDTO> orderedDishes = new ArrayList<>();
        double totalPriceSEK = 0.0;
        double totalPriceYEN = 0.0;

        for (BookingDetails detail : bookingDetails) {
            Dishes dish = detail.getDish();
            OrderItemDTO orderItemDTO = new OrderItemDTO(dish.getId(), dish.getName(), detail.getQuantity());
            orderedDishes.add(orderItemDTO);

            totalPriceSEK += detail.getPriceSEK();
            totalPriceYEN += detail.getPriceYEN();
        }

        ShowBookingDTO showBookingDTO = new ShowBookingDTO();
        showBookingDTO.setId(booking.getId());
        showBookingDTO.setCustomer(customer);
        showBookingDTO.setRoomName(room.getName());
        showBookingDTO.setRoomId(room.getId());
        showBookingDTO.setRoomDescription(room.getDescription());
        showBookingDTO.setGuestCount(booking.getGuests());
        showBookingDTO.setOrderedDishes(orderedDishes);
        showBookingDTO.setTotalPriceSEK(totalPriceSEK);
        showBookingDTO.setTotalPriceYEN(totalPriceYEN);
        showBookingDTO.setBookingDate(booking.getBookingDate());

        return showBookingDTO;
    }

    public SushiBooking updateReservation(int bookingId,int roomId, int guests, List<Integer> dishIds, List<Integer> quantities) {
        Optional<SushiBooking> existingBookingOptional = bookingRepository.findById(bookingId);
        int guestsInRoom = roomService.getGuestsInRoom(roomId);

        if (existingBookingOptional.isEmpty()) {
            throw new IllegalArgumentException("Bokningen finns inte.");
        }
        SushiRoom room = roomService.findRoomById(roomId);

        if (guestsInRoom + guests > room.getMaxGuests()) {
            throw new IllegalArgumentException("För många gäster för det valda rummet.");
        }

        SushiBooking existingBooking = existingBookingOptional.get();

        List<BookingDetails> bookingDetailsToRemove = new ArrayList<>(existingBooking.getBookingDetails());

        for (BookingDetails bookingDetail : bookingDetailsToRemove) {
            existingBooking.getBookingDetails().remove(bookingDetail);
            bookingDetailsRepository.delete(bookingDetail);
        }

        existingBooking.getBookingDetails().clear();

        existingBooking.setGuests(guests);
        existingBooking.setRoom(room);
        existingBooking.setBookingDate(new Date());
        existingBooking.setTotalPriceSEK(0.0);

        List<BookingDetails> updatedBookingDetails = new ArrayList<>();

        double totalSEKPrice = 0.0;
        int totalYENPrice = 0;

        for (int i = 0; i < dishIds.size(); i++) {
            int dishId = dishIds.get(i);
            int quantity = quantities.get(i);

            Dishes dish = dishesService.findDishById(dishId);

            if (dish == null) {
                throw new IllegalArgumentException("Felaktigt rätt-ID: " + dishId);
            }

            CurrencyConverter currency = new CurrencyConverter();

            double dishSEKPrice = dish.getSekPrice() * quantity;
            totalSEKPrice += dishSEKPrice;
            try{
                totalYENPrice = (int) CurrencyConverter.SekToRequestedCurrency(dishSEKPrice, "JPY");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BookingDetails updatedBookingDetail = new BookingDetails();
            updatedBookingDetail.setCustomer(existingBooking.getCustomer());
            updatedBookingDetail.setBooking(existingBooking);
            updatedBookingDetail.setGuests(guests);
            updatedBookingDetail.setRoom(existingBooking.getRoom());
            updatedBookingDetail.setDish(dish);
            updatedBookingDetail.setQuantity(quantity);
            updatedBookingDetail.setPriceSEK(dishSEKPrice);
            try{
                updatedBookingDetail.setPriceYEN((int) (CurrencyConverter.SekToRequestedCurrency(dishSEKPrice, "JPY")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            updatedBookingDetail.setBookingDate(existingBooking.getBookingDate());

            updatedBookingDetails.add(updatedBookingDetail);
        }

        existingBooking.setTotalPriceSEK(totalSEKPrice);
        existingBooking.setTotalPriceYEN(totalYENPrice);

        existingBooking.getBookingDetails().addAll(updatedBookingDetails);

        return bookingRepository.save(existingBooking);
    }

    @Override
    public SushiBooking createReservation(int customerId, int roomId, int guests, List<Integer> dishIds, List<Integer> quantities) {
        Customer customer = customerService.findCustomerById(customerId);
        int guestsInRoom = roomService.getGuestsInRoom(roomId);

        if (customer == null) {
            throw new IllegalArgumentException("Felaktigt kund-ID.");
        }

        SushiRoom room = roomService.findRoomById(roomId);

        if (room == null) {
            throw new IllegalArgumentException("Felaktigt rum-ID: " + roomId);
        }

        if (guestsInRoom + guests > room.getMaxGuests()) {
            throw new IllegalArgumentException("För många gäster för det valda rummet.");
        }

        Date bookingDate = new Date();

        List<BookingDetails> bookingDetailsList = new ArrayList<>();
        double totalSEKPrice = 0.0;
        int totalYENPrice = 0;

        SushiBooking booking = new SushiBooking(customer, guests, room, totalSEKPrice, bookingDate);

        for (int i = 0; i < dishIds.size(); i++) {
            int dishId = dishIds.get(i);
            int quantity = quantities.get(i);

            Dishes dish = dishesService.findDishById(dishId);

            if (dish == null) {
                throw new IllegalArgumentException("Felaktigt rätt-ID: " + dishId);
            }

            double dishSEKPrice = dish.getSekPrice() * quantity;
            totalSEKPrice += dishSEKPrice;
            try{
                totalYENPrice = (int) (CurrencyConverter.SekToRequestedCurrency(totalSEKPrice, "JPY"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            booking.setTotalPriceSEK(totalSEKPrice);
            booking.setTotalPriceYEN(totalYENPrice);
            booking.setGuests(guests);
            booking.setRoom(room);
            booking.setCustomer(customer);

            BookingDetails bookingDetails = new BookingDetails();
            bookingDetails.setCustomer(customer);
            bookingDetails.setBooking(booking);
            bookingDetails.setGuests(guests);
            bookingDetails.setRoom(room);
            bookingDetails.setDish(dish);
            bookingDetails.setQuantity(quantity);
            bookingDetails.setPriceSEK(dishSEKPrice);
            try{
                bookingDetails.setPriceYEN((int) CurrencyConverter.SekToRequestedCurrency(dishSEKPrice, "JPY"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            bookingDetails.setBookingDate(bookingDate);

            bookingDetailsList.add(bookingDetails);
        }

        booking.setBookingDetails(bookingDetailsList);
        bookingRepository.save(booking);

        return booking;
    }

    @Override
    public SushiBooking saveBooking(SushiBooking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public List<BookingDetails> findBookingDetailsContainingDish(Dishes dish) {
        return bookingRepository.findBookingDetailsContainingDish(dish);
    }

}


