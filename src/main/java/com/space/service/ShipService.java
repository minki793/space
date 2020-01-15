package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ShipService {
    Ship addShip(Ship ship);
    void delete(Ship ship);
    Optional<Ship> getById(Long id);
    Ship editShip(Ship bank);
    List<Ship> getAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                             Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, PageRequest pageable);
}
