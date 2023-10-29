package com.Grupparbete.API.Service;

import com.Grupparbete.API.CurrencyConverter;
import com.Grupparbete.API.DAO.TripRepository;
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
public class TripServiceImpl implements TripService {

    private final Logger logger = LogManager.getLogger("MyLogger");
    private TripRepository tripRepository;
    private TripDestinationService tripDestinationService;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, TripDestinationService tripDestinationService) {
        this.tripRepository = tripRepository;
        this.tripDestinationService = tripDestinationService;
    }

    @Override
    public List<Trip> findAll() {
        List<Trip> trips = tripRepository.findAll();
        List<Trip> inspectedListOfTrips = new ArrayList<>();
        for (Trip t : trips) {
            try {
                t = makeSureCurrencyIsUpdated(t, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            inspectedListOfTrips.add(t);
        }
        return inspectedListOfTrips;
    }


    @Override
    public Trip findById(int id) {
        Optional<Trip> t = tripRepository.findById(id);
        Trip trip;
        if (t.isPresent()) {
            trip = t.get();
            try {
                makeSureCurrencyIsUpdated(trip, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Trip with id: " + id + " could not be found!");
        }
        return trip;
    }

    @Override
    @Transactional
    public Trip save(Trip trip) {
        trip.setDestination(tripDestinationService.checkIfExistsInDatabaseIfNotSave(trip.getDestination(), true));
        trip.setTotalPriceSEK(trip.getDestination().getPricePerWeek() * trip.getNumberOfWeeks());
        try {
            trip.setTotalPricePLN(CurrencyConverter.SekToRequestedCurrency(trip.getTotalPriceSEK(), "PLN"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Trip savedTrip = tripRepository.save(trip);
        logger.info("Trip saved: " + savedTrip);
        return savedTrip;
    }

    @Override
    public Trip update(int id, Trip trip) {
        Trip tripFromDb = findById(id);
        Trip newTrip = new Trip();
        newTrip.setTripId(tripFromDb.getTripId());
        newTrip.setDestination(tripDestinationService.checkIfExistsInDatabaseIfNotSave(trip.getDestination(), true));
        newTrip.setNumberOfWeeks(trip.getNumberOfWeeks());
        newTrip.setTotalPriceSEK(trip.getTotalPriceSEK());
        newTrip.setTotalPricePLN(trip.getTotalPricePLN());
        newTrip.setDepartureDate(trip.getDepartureDate());
        logger.info("Trip edited \nFrom: " + tripFromDb + "\nTo: " + newTrip);
        return save(newTrip);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Trip trip = findById(id);
        Customer customer = tripRepository.findCustomerByTripId(id);
        if (customer != null) {
            customer.getTrips().remove(trip);
        }
        trip.setDestination(null);
        tripRepository.deleteById(id);
    }


    @Override
    public List<Trip> inspectTripList(List<Trip> tripList, int customerId) {
        List<Trip> inspectedTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            Trip t = checkIfExistsInDatabaseIfNotSave(trip, customerId, true);

            if (!inspectedTripList.contains(t)) {
                inspectedTripList.add(t);
            }
        }
        return inspectedTripList;
    }


    @Override
    public List<Trip> findTripsByCustomerId(int customerId) {
        return tripRepository.findTripsByCustomerId(customerId);
    }

    private Trip checkIfExistsInDatabaseIfNotSave(Trip trip, int customerId, boolean autoSave) {
        if (trip.getTripId() > 0) {
            return update(trip.getTripId(), trip);
        }
        Trip tripFromDatabase = tripRepository.findTripByCustomerIdAndDepartureDate(trip.getTripId(), customerId);
        if (tripFromDatabase != null) {
            if (!trip.equals(tripFromDatabase)) {
                tripFromDatabase.setDepartureDate(trip.getDepartureDate());
                tripFromDatabase.setNumberOfWeeks(trip.getNumberOfWeeks());
                tripFromDatabase.setTotalPriceSEK(trip.getTotalPriceSEK());
                tripFromDatabase.setTotalPricePLN(trip.getTotalPricePLN());
                tripFromDatabase.setDestination(tripDestinationService.checkIfExistsInDatabaseIfNotSave(trip.getDestination(), true));
            }
            return tripFromDatabase;
        }

        trip.setDestination(tripDestinationService.checkIfExistsInDatabaseIfNotSave(trip.getDestination(), true));
        if (autoSave) {
            trip = save(trip);
        }
        return trip;
    }

    @Override
    @Transactional
    public Trip makeSureCurrencyIsUpdated(Trip trip, boolean autoSave) throws IOException {
        double totSEK = trip.getDestination().getPricePerWeek() * trip.getNumberOfWeeks();
        double totPLN = CurrencyConverter.SekToRequestedCurrency(totSEK, "PLN");

        if (trip.getTotalPricePLN() != totPLN) {
            trip.setTotalPriceSEK(Math.round(totSEK));
            trip.setTotalPricePLN(totPLN);
            if (autoSave) {
                return save(trip);
            }
        }
        return trip;
    }
}
