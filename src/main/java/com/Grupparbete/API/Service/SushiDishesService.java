package com.Grupparbete.API.Service;

import com.Grupparbete.API.Entities.Dishes;

import java.util.List;

public interface SushiDishesService {
        Dishes addDish(Dishes dishes);
        List<Dishes> findAllDishes();
        Dishes findDishById(int id);
        Dishes updateDish(Dishes dishes, int id);
        void deleteDish(int id);
    }

