package com.Grupparbete.API.Service;


import com.Grupparbete.API.CurrencyConverter;
import com.Grupparbete.API.DAO.DishesRepository;
import com.Grupparbete.API.Entities.Dishes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DishesServiceImpl implements DishesService {
    private DishesRepository dishesRepository;

    @Autowired
    public DishesServiceImpl(DishesRepository dishesRepository) {
        this.dishesRepository = dishesRepository;
    }

    @Override
    public Dishes addDish(Dishes dishes) {
        return dishesRepository.save(dishes);
    }

    @Override
    public List<Dishes> findAllDishes() {
        CurrencyConverter converter = new CurrencyConverter();
        List<Dishes> dishes = dishesRepository.findAll();
        dishes.forEach(dish -> {
            double priceInSEK = dish.getSekPrice();
            double exchangeRate = 0;
            try {
                exchangeRate = converter.SekToRequestedCurrency(priceInSEK, "JPY");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            double priceInYEN = priceInSEK * exchangeRate;
            int priceInYENRoundedUp = (int) Math.ceil(priceInYEN);
            dish.setYenPrice(priceInYENRoundedUp);
        });

        return dishes;
    }


    @Override
    public Dishes findDishById(int id) {
        return dishesRepository.findById(id).orElse(null);
    }

    @Override
    public Dishes updateDish(Dishes dishes, int id) {
        if (dishesRepository.existsById(id)) {
            dishes.setId(id);
            return dishesRepository.save(dishes);
        } else {
            return null;
        }
    }

    @Override
    public void deleteDish(int id) {
        dishesRepository.deleteById(id);
    }
}
