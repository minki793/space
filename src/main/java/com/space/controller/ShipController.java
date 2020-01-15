package com.space.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class ShipController {

    private ShipService shipService;

    @GetMapping("/ships")
    public List<Ship> shipList(@RequestParam(value="name", required=false) String name,
                               @RequestParam(value="planet", required=false) String planet,
                               @RequestParam(value="shipType", required=false) ShipType shipType,
                               @RequestParam(value="after", required=false) Long after,
                               @RequestParam(value="before", required=false) Long before,
                               @RequestParam(value="isUsed", required=false) Boolean isUsed,
                               @RequestParam(value="minSpeed", required=false) Double minSpeed,
                               @RequestParam(value="maxSpeed", required=false) Double maxSpeed,
                               @RequestParam(value="minCrewSize", required=false) Integer minCrewSize,
                               @RequestParam(value="maxCrewSize", required=false) Integer maxCrewSize,
                               @RequestParam(value="minRating", required=false) Double minRating,
                               @RequestParam(value="maxRating", required=false) Double maxRating,
                               @RequestParam(value="order", required=false, defaultValue = "ID") ShipOrder order,
                               @RequestParam(value="pageNumber", required=false, defaultValue = "0") Integer pageNumber,
                               @RequestParam(value="pageSize", required=false, defaultValue = "3") Integer pageSize) {


        PageRequest pageable = PageRequest.of(pageNumber, pageSize, new Sort(Sort.Direction.ASC, order.getFieldName()));
        List<Ship> shipList = shipService.getAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize,
                minRating, maxRating, pageable);
        return shipList;
    }

    @GetMapping("/ships/count")
    public Integer shipListSize(@RequestParam(value="name", required=false) String name,
                           @RequestParam(value="planet", required=false) String planet,
                           @RequestParam(value="shipType", required=false) ShipType shipType,
                           @RequestParam(value="after", required=false) Long after,
                           @RequestParam(value="before", required=false) Long before,
                           @RequestParam(value="isUsed", required=false) Boolean isUsed,
                           @RequestParam(value="minSpeed", required=false) Double minSpeed,
                           @RequestParam(value="maxSpeed", required=false) Double maxSpeed,
                           @RequestParam(value="minCrewSize", required=false) Integer minCrewSize,
                           @RequestParam(value="maxCrewSize", required=false) Integer maxCrewSize,
                           @RequestParam(value="minRating", required=false) Double minRating,
                           @RequestParam(value="maxRating", required=false) Double maxRating) {


        List<Ship> shipList = shipService.getAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize,
                minRating, maxRating, null);
        return shipList.size();
    }



    @PostMapping("/ships/{id}")
    public Ship updateShip(@PathVariable String id,
                         @RequestBody Optional<Ship> ship) {
        if (valid(id)) {
            Ship shipOp = ship.get();
            if (!validParams(shipOp)) {
                throw new Excepton400();
            }
            Optional<Ship> shipOptional = shipService.getById(Long.parseLong(id));
            if (shipOptional.isPresent()) {
                Ship shipEd = shipOptional.get();
                if (shipOp.getName() != null) {
                    shipEd.setName(shipOp.getName());
                }
                if (shipOp.getPlanet() != null) {
                    shipEd.setPlanet(shipOp.getPlanet());
                }
                if (shipOp.getShipType() != null) {
                    shipEd.setShipType(shipOp.getShipType());
                }
                if (shipOp.getProdDate() != null) {
                    shipEd.setProdDate(shipOp.getProdDate());
                }
                if (shipOp.getUsed() != null) {
                    shipEd.setUsed(shipOp.getUsed());
                }
                if (shipOp.getSpeed() != null) {
                    shipEd.setSpeed(shipOp.getSpeed());
                }
                if (shipOp.getCrewSize() != null) {
                    shipEd.setCrewSize(shipOp.getCrewSize());
                }
                return shipService.editShip(shipEd);
            } else {
                throw new Excepton404();
            }
        }
        throw new Excepton400();
    }

    private boolean valid(String id) {
        try {
            if (id.equals("0")) {
                return false;
            }
            Long longId = Long.parseLong(id);
            if (longId < 0) {
                return false;
            }
            if (Math.round(longId) != longId) {
                return false;
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private boolean validParams(Ship shipAdd) {
        try {
            String name = shipAdd.getName();
            String planet = shipAdd.getPlanet();
            Date date = shipAdd.getProdDate();
            Double speed = shipAdd.getSpeed();
            Integer crewSize = shipAdd.getCrewSize();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");

            if (name != null && (name.length() > 50 || name.equals(""))) {
                return false;
            }
            if (planet != null && (planet.length() > 50 || planet.equals(""))) {
                return false;
            }
            if (speed != null && (speed < 0.1 || speed > 0.99)) {
                return false;
            }
            if (crewSize != null && (crewSize < 1 || crewSize > 9999)) {
                return false;
            }
            if (date != null) {
                int dateYear = Integer.parseInt(dateFormat.format(date));
                if (dateYear < 2800 || dateYear > 3019) {
                    return false;
                }
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private boolean nullParams(Ship shipAdd) {
        try {
            Long id = shipAdd.getId();
            String name = shipAdd.getName();
            String planet = shipAdd.getPlanet();
            ShipType shipType = shipAdd.getShipType();
            Date date = shipAdd.getProdDate();
            Boolean isUsed = shipAdd.getUsed();
            Double speed = shipAdd.getSpeed();
            Integer crewSize = shipAdd.getCrewSize();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            int dateYear = Integer.parseInt(dateFormat.format(date));
            if (name.equals("") || planet.equals("") || name == null || planet == null || shipType == null ||
                    date == null || speed == null || crewSize == null) {
                return true;
            }
            return false;
        }catch (Exception e) {
            return true;
        }
    }

    @PostMapping("/ships")
    public Ship createShip(@RequestBody Optional<Ship> shipOptional) {
        if (shipOptional.isPresent()) {
            Ship shipAdd = shipOptional.get();
            if (!nullParams(shipAdd) && validParams(shipAdd)) {
                if (shipAdd.getUsed() == null) {
                    shipAdd.setUsed(false);
                }
                shipAdd.setRating();
                return shipService.addShip(shipAdd);
            } else {
                throw new Excepton400();
            }
        }
        throw new Excepton400();
    }


    @GetMapping("/ships/{id}")
    public Ship shipById(@PathVariable String id) {
        if (valid(id)) {
            Optional<Ship> shipOptional = shipService.getById(Long.parseLong(id));
            if (shipOptional.isPresent()) {
                Ship ship = shipOptional.get();
                return ship;
            } else {
                throw new Excepton404();
            }
        }
        throw new Excepton400();
    }


    @DeleteMapping ("/ships/{id}")
    public HttpStatus shipDelete(@PathVariable String id) {
        if (valid(id)) {
            Optional<Ship> shipOptional = shipService.getById(Long.parseLong(id));
            if (shipOptional.isPresent()) {
                Ship ship = shipOptional.get();
                shipService.delete(ship);
                return HttpStatus.OK;
            } else {
                throw new Excepton404();
            }
        }
        throw new Excepton400();
    }


    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }
}
