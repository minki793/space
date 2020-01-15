package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ShipServiceImpl implements ShipService {
    private ShipRepository shipRepository;

    @Override
    public Ship addShip(Ship ship) {
        Ship savedBank = shipRepository.saveAndFlush(ship);
        return savedBank;
    }

    @Override
    public void delete(Ship ship) {
        shipRepository.delete(ship);
    }

    @Override
    public Optional<Ship> getById(Long id) {
        return shipRepository.findById(id);
    }

    @Override
    public Ship editShip(Ship ship) {
        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public List<Ship> getAll(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                             Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating, PageRequest pageable) {

        List<SearchCriteria> params = new ArrayList<>();
        if (name != null) {
            params.add(new SearchCriteria("name", ":", name));
        }
        if (planet != null) {
            params.add(new SearchCriteria("planet", ":", planet));
        }
        if (shipType != null) {
            params.add(new SearchCriteria("shipType", ":", shipType));
        }
        if (after != null) {
            params.add(new SearchCriteria("prodDate", "dt>=", after));
        }
        if (before != null) {
            params.add(new SearchCriteria("prodDate", "dt<", before));
        }
        if (isUsed != null) {
            params.add(new SearchCriteria("isUsed", ":", isUsed));
        }
        if (minSpeed != null) {
            params.add(new SearchCriteria("speed", ">=", minSpeed));
        }
        if (maxSpeed != null) {
            params.add(new SearchCriteria("speed", "<=", maxSpeed));
        }
        if (minCrewSize != null) {
            params.add(new SearchCriteria("crewSize", ">=", minCrewSize));
        }
        if (maxCrewSize != null) {
            params.add(new SearchCriteria("crewSize", "<=", maxCrewSize));
        }
        if (minRating != null) {
            params.add(new SearchCriteria("rating", ">=", minRating));
        }
        if (maxRating != null) {
            params.add(new SearchCriteria("rating", "<=", maxRating));
        }
        if (params.size() > 0) {
            Specification result = new Filter(params.get(0));

            for (int i = 1; i < params.size(); i++) {
                result = Specification.where(result).and(new Filter(params.get(i)));
            }
            if (pageable == null) {
                return shipRepository.findAll(result);
            }
            return shipRepository.findAll(result, pageable).getContent();
        } else {
            if (pageable == null) {
                return shipRepository.findAll();
            }
            return shipRepository.findAll(pageable).getContent();
        }
    }


    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }
}

