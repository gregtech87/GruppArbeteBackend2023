package com.Grupparbete.API.Service;

import com.Grupparbete.API.DAO.AddressRepository;
import com.Grupparbete.API.Entities.Address;
import com.Grupparbete.API.Service.AddressService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final Logger logger = LogManager.getLogger("MyLogger");
    private AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address findAddressById(int id) {
        Optional<Address> a = addressRepository.findById(id);
        Address address = new Address();
        if (a.isPresent()){
            address = a.get();
        }
        return address;
    }

    @Override
    @Transactional
    public Address saveAddress(Address address) {
        Address savedAddress = addressRepository.save(address);
        logger.info("Address was saved: " + savedAddress);
        return savedAddress;
    }

    @Override
    public Address updateAddress(int id, Address address) {
        Address addressFromDatabase = findAddressById(address.getId());

        Address newAddress = new Address();
        newAddress.setId(addressFromDatabase.getId());
        newAddress.setPostalCode(address.getPostalCode());
        newAddress.setStreet(address.getStreet());
        newAddress.setCity(address.getCity());

        logger.info("Address was edited " + "\nFrom: " + addressFromDatabase + "\nTo: " + newAddress);
        return saveAddress(newAddress);
    }

    @Override
    @Transactional
    public void deleteAddressById(int id) {
        logger.info("Address was deleted: " + findAddressById(id));
        addressRepository.deleteById(id);
    }
    @Override
    public Address findAddressByStreetAndPostalCodeAndCity(String street, int postalCode, String city) {
        return addressRepository.findAddressByStreetAndPostalCodeAndCity(street, postalCode, city);
    }
    @Override
    @Transactional
    public Address checkIfExistsInDatabaseIfNotSave(Address address, boolean autoSave) {
        final Address baseAddress =new Address();
        baseAddress.setId(address.getId());
        baseAddress.setCity(address.getCity());
        baseAddress.setStreet(address.getStreet());
        baseAddress.setPostalCode(address.getPostalCode());

        String street = address.getStreet();
        int postalCode = address.getPostalCode();
        String city = address.getCity();

        if (baseAddress.getId() > 0){
            return updateAddress(baseAddress.getId(), address);
        }
        Address addressFromDatabase = findAddressByStreetAndPostalCodeAndCity(street, postalCode, city);
        if (addressFromDatabase != null){
            return addressFromDatabase;
        }
        if (autoSave) {
            Address savedAddress = saveAddress(address);
            return savedAddress;
        }
        return address;
    }
}

