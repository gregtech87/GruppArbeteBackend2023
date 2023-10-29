package com.Grupparbete.API.Service;


import com.Grupparbete.API.DAO.DestinationRepository;
import com.Grupparbete.API.Entities.Destination;
import com.Grupparbete.API.Entities.Trip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TripDestinationServiceImpl implements TripDestinationService {

    private final Logger logger = LogManager.getLogger("MyLogger");
    private DestinationRepository destinationRepository;

    @Autowired
    public TripDestinationServiceImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public List<Destination> findAll() {
        return destinationRepository.findAll();
    }

    @Override
    public Destination findById(int id) {
        Optional<Destination> d = destinationRepository.findById(id);
        Destination destination = new Destination();
        if (d.isPresent()){
            destination = d.get();
        }
        return destination;
    }

    @Override
    @Transactional
    public Destination save(Destination destination) {
//        destination = checkIfExistsInDatabaseIfNotSave(destination, false);
        Destination savedDestination = destinationRepository.save(destination);
        logger.info("Destination saved: " + savedDestination);
        return savedDestination;
    }

    @Override
    public Destination updateDestination(int id, Destination destination) {
        Destination destinationFromDb = findById(id);
        destinationFromDb.setHotellName(destination.getHotellName());
        destinationFromDb.setPricePerWeek(destination.getPricePerWeek());
        destinationFromDb.setCity(destination.getCity());
        destinationFromDb.setCountry(destination.getCountry());
        destination.setId(id);
        logger.info("Destination edited \nFrom: " + destination + "\nTo: " + destinationFromDb);
        return save(destinationFromDb);
    }

    @Override
    @Transactional
    public String deleteById(int id) {
        String message = "";
        List<Trip> tripList = destinationRepository.findAllByDestination(findById(id).getId());
        System.out.println("RESA BASERAT PÅÅ RESA: " + tripList);
        System.out.println(tripList.size());
        if (tripList.size() == 0){
            message = "Destination with id: " + id + " has been deleted!";
            logger.info("Destination was deleted: " + findById(id));
            destinationRepository.deleteById(id);
            return message;
        }else {
            message = "Destination with id: " + id + " can not be deleted! Destination is used in " + tripList.size() + " trips!";
            for (Trip t : tripList){
                String tripId = "\n" + "Trip ID: " + String.valueOf(t.getTripId());
                message = message.concat(tripId);
            }
            logger.info(message);
        }
        return message;
    }

    @Override
    @Transactional
    public Destination checkIfExistsInDatabaseIfNotSave(Destination destination, boolean autoSave) {

        String city = destination.getCity();
        String country = destination.getCountry();
        String hotellName = destination.getHotellName();

        if (destination.getId() > 0){
            return updateDestination(destination.getId(),destination);
        }
        Destination destinationFromDatabase = destinationRepository.findDestinationByHotellNameAndCityAndCountry(hotellName, city, country);
        if (destinationFromDatabase != null){
            System.out.println("FRÅN DB: " + destinationFromDatabase);
            return destinationFromDatabase;
        }
        if(autoSave){
            destinationFromDatabase = save(destination);
            logger.info("Destination was edited from: " + destination + "\nTo: " + destinationFromDatabase);
            System.out.println("SPARAD: " + destinationFromDatabase);
            return destinationFromDatabase;
        }
        System.out.println("###############################################################################");
      return destination;
    }
}
