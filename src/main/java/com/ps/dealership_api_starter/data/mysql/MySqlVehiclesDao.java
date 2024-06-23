package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.VehiclesDao;
import com.ps.dealership_api_starter.models.Vehicle;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component

public class MySqlVehiclesDao extends MySqlDaoBase implements VehiclesDao {

    public MySqlVehiclesDao(DataSource dataSource) {

        super(dataSource);

    }

    @Override
    public List<Vehicle> search(double minPrice, double maxPrice, String make, String model, int minYear, int maxYear, String color, int minMiles, int maxMiles, String type) {

        List<Vehicle> vehicles = new ArrayList<>();

        String query = "SELECT * FROM Vehicles " +
                "WHERE (Price BETWEEN ? AND ?)" +
                "AND (Make LIKE ?)" +
                "AND (Model LIKE ?)" +
                "AND (Year BETWEEN ? AND ?)" +
                "AND (Color LIKE ?)" +
                "AND (Odometer BETWEEN ? AND ?)" +
                "AND (Vehicle_Type LIKE ?)";

        double minPriceToSearch = minPrice == 0 ? -1 : minPrice;
        double maxPriceToSearch = maxPrice == 0 ? -1 : maxPrice;
        String makeToSearch = make == null ? "%" : make;
        String modelToSearch = model == null ? "%" : model;
        int minYearToSearch = minYear == 0 ? -1 : minYear;
        int maxYearToSearch = maxYear == 0 ? -1 : maxYear;
        String colorToSearch = color == null ? "%" : color;
        int minMilesToSearch = minMiles == 0 ? -1 : minMiles;
        int maxMilesToSearch = maxMiles == 0 ? -1 : maxMiles;
        String typeToSearch = type == null ? "%" : type;

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDouble(1, minPriceToSearch);
            preparedStatement.setDouble(2, maxPriceToSearch);

            preparedStatement.setString(3, "%" + makeToSearch + "%");
            preparedStatement.setString(4, "%" + modelToSearch + "%");

            preparedStatement.setInt(5, minYearToSearch);
            preparedStatement.setInt(6, maxYearToSearch);

            preparedStatement.setString(7, "%" + colorToSearch + "%");

            preparedStatement.setInt(8, minMilesToSearch);
            preparedStatement.setInt(9, maxMilesToSearch);

            preparedStatement.setString(10, "%" + typeToSearch + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Vehicle vehicle = mapResultSet(resultSet);
                vehicles.add(vehicle);
            }

        } catch(SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);

        }

        return vehicles;
    }

    @Override
    public Vehicle getByVin(int vin) {

        Vehicle vehicle = null;

        String sql = "SELECT * FROM Vehicles WHERE vin = ?";

        try (Connection connection = getConnection())

        {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, vin);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {

                vehicle = mapResultSet(resultSet);

            }
        }

        catch (SQLException e)

        {

            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return vehicle;
    }

    @Override
    public Vehicle create(Vehicle vehicle) {

        String query = "INSERT INTO Vehicles(vin, year, make, model, vehicle_type, color, odometer, price, sold) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1,vehicle.getVin());
            preparedStatement.setInt(2, vehicle.getYear());

            preparedStatement.setString(3, vehicle.getMake());
            preparedStatement.setString(4, vehicle.getModel());
            preparedStatement.setString(5, vehicle.getVehicleType());

            preparedStatement.setString(6, vehicle.getColor());

            preparedStatement.setInt(7, vehicle.getOdometer());

            preparedStatement.setDouble(8, vehicle.getPrice());

            preparedStatement.setBoolean(9, vehicle.isSold());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if(generatedKeys.next()) {

                    int vin = generatedKeys.getInt(1);

                    return getByVin(vin);
                }
            }

        } catch(SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return null;
    }

    @Override
    public void updateVehicle(int vin, Vehicle vehicle) {

        String query = "UPDATE vehicles SET year = ?, make = ?, model = ?, vehicle_type = ?, " +
                "color = ?, odometer = ?, price = ?, sold = ? " +
                "WHERE vin = ?";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, vehicle.getYear());

            preparedStatement.setString(2, vehicle.getMake());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setString(4, vehicle.getVehicleType());
            preparedStatement.setString(5, vehicle.getColor());

            preparedStatement.setInt(6, vehicle.getOdometer());
            preparedStatement.setDouble(7, vehicle.getPrice());

            preparedStatement.setBoolean(8, vehicle.isSold());

            preparedStatement.setInt(9, vin);

            preparedStatement.executeUpdate();

        } catch(SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    @Override
    public void deleteVehicle(int vin) {

        String query = "DELETE FROM Vehicles WHERE vin = ?";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vin);
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    protected static Vehicle mapResultSet(ResultSet resultSet) throws SQLException {

        int vin = resultSet.getInt("vin");
        int year = resultSet.getInt("year");
        String make = resultSet.getString("make");
        String model = resultSet.getString("model");
        String type = resultSet.getString("vehicle_type");
        String color = resultSet.getString("color");
        int odometer = resultSet.getInt("odometer");
        double price = resultSet.getDouble("price");
        boolean sold = resultSet.getBoolean("sold");


        return new Vehicle(vin, year, make, model, type, color, odometer, price, sold);
    }

}