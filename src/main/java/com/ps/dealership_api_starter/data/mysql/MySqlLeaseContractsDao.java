package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.LeaseContractsDao;
import com.ps.dealership_api_starter.models.LeaseContract;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlLeaseContractsDao extends MySqlDaoBase implements LeaseContractsDao {

    public MySqlLeaseContractsDao(DataSource dataSource) {

        super(dataSource);

    }

    @Override
    public LeaseContract getByLeaseContractId(int contract_id) {

        LeaseContract leaseContract = null;

        String query = "SELECT * FROM Lease_Contracts WHERE contract_id = ?";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, contract_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                leaseContract = mapResultSet(resultSet);

            }

        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return leaseContract;
    }

    @Override
    public LeaseContract addLeaseContract(LeaseContract leaseContract) {

        String query = "INSERT INTO lease_contracts(contract_date, customer_name, customer_email," +
                "vin, sales_tax, recording_fee, processing_fee, total_price, monthly_payment)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, leaseContract.getContractDate());
            preparedStatement.setString(2, leaseContract.getCustomerName());
            preparedStatement.setString(3, leaseContract.getCustomerEmail());

            preparedStatement.setInt(4, leaseContract.getVin());

            preparedStatement.setDouble(5, leaseContract.getSalesTax());

            preparedStatement.setDouble(6, leaseContract.getRecordingFee());
            preparedStatement.setDouble(7, leaseContract.getProcessingFee());
            preparedStatement.setDouble(8, leaseContract.getTotalPrice());
            preparedStatement.setDouble(9, leaseContract.getMonthlyPayment());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if(generatedKeys.next()) {
                    int contractId = generatedKeys.getInt(1);

                    return getByLeaseContractId(contractId);
                }
            }

        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    protected static LeaseContract mapResultSet(ResultSet resultSet) throws SQLException {

        int contractId = resultSet.getInt("contract_id");
        String date = resultSet.getString("contract_date");
        String customerName = resultSet.getString("customer_name");
        String customerEmail = resultSet.getString("customer_email");
        int vin = resultSet.getInt("vin");
        double salesTax = resultSet.getDouble("sales_tax");
        double recordingFee = resultSet.getDouble("recording_fee");
        double processingFee = resultSet.getDouble("processing_fee");
        double totalPrice = resultSet.getDouble("total_price");
        double monthlyPayment = resultSet.getDouble("monthly_payment");

        return new LeaseContract(contractId, date, customerName, customerEmail, vin , salesTax,
                recordingFee, processingFee, totalPrice, monthlyPayment);
    }
}