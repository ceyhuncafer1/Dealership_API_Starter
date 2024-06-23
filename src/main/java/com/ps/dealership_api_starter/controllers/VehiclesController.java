package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.VehiclesDao;
import com.ps.dealership_api_starter.models.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin
public class VehiclesController {

    private VehiclesDao vehiclesDao;

    @Autowired
    public VehiclesController(VehiclesDao vehiclesDao) {

        this.vehiclesDao = vehiclesDao;

    }

    @GetMapping("")
    public List<Vehicle> search(@RequestParam(name = "minPrice", required = false) Double minPrice,
                                @RequestParam(name = "maxPrice", required = false) Double maxPrice,
                                @RequestParam(name = "make", required = false) String make,
                                @RequestParam(name = "model", required = false) String model,
                                @RequestParam(name = "minYear", required = false) Integer minYear,
                                @RequestParam(name = "maxYear", required = false) Integer maxYear,
                                @RequestParam(name = "color", required = false) String color,
                                @RequestParam(name = "minMiles", required = false) Integer minMiles,
                                @RequestParam(name = "maxMiles", required = false) Integer maxMiles,
                                @RequestParam(name = "type", required = false) String type
    ) {
        try {

            return vehiclesDao.search(minPrice, maxPrice, make, model, minYear, maxYear, color, minMiles, maxMiles, type);

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

    @GetMapping("{vin}")
    public Vehicle getByVin(@PathVariable int vin) {

        try {

            Vehicle vehicle = vehiclesDao.getByVin(vin);

            if (vehicle == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return vehicle;

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

    @PostMapping()
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {

        try {

            return vehiclesDao.create(vehicle);

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }

    @PutMapping("{vin}")
    public void updateVehicle(@PathVariable int vin, @RequestBody Vehicle vehicle) {

        try {

            vehiclesDao.updateVehicle(vin, vehicle);

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }

    }

    @DeleteMapping("{vin}")
    public void deleteVehicle(@PathVariable int vin) {

        try {

            Vehicle vehicle = vehiclesDao.getByVin(vin);

            if (vehicle == null) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            }

            vehiclesDao.deleteVehicle(vin);

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

        }
    }
}