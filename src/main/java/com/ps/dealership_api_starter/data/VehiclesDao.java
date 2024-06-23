package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.Vehicle;

import java.util.List;

public interface VehiclesDao {

    List<Vehicle> search(double minPrice, double maxPrice, String make, String model, int minYear, int maxYear, String color, int minMiles, int maxMiles, String type);
    Vehicle getByVin(int vin);
    Vehicle create(Vehicle vehicle);
    void updateVehicle(int vin, Vehicle vehicle);
    void deleteVehicle(int vin);

}
