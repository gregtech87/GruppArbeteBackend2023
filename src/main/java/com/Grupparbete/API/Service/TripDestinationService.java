package com.Grupparbete.API.Service;



import com.Grupparbete.API.Entities.Destination;
import java.util.List;

public interface TripDestinationService {
    List<Destination> findAll();
    Destination findById(int id);
    Destination save(Destination destination);
    Destination updateDestination(int id, Destination destination);
    String deleteById(int id);
    Destination checkIfExistsInDatabaseIfNotSave(Destination destination, boolean autoSave);
}
