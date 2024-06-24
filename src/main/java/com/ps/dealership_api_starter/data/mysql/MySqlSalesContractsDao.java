package com.ps.dealership_api_starter.data.mysql;

import com.ps.dealership_api_starter.data.SalesContractsDao;
import com.ps.dealership_api_starter.models.SalesContract;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlSalesContractsDao extends MySqlDaoBase implements SalesContractsDao{

    public MySqlSalesContractsDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public SalesContract getBySalesContractId(int contract_id) {

        SalesContract salesContract = null;

        String query = "SELECT * FROM Sales_Contracts WHERE contract_id = ?";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, contract_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                salesContract = mapResultSet(resultSet);
            }

        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return salesContract;
    }

    @Override
    public SalesContract createSalesContract(SalesContract salesContract) {

        String query = "INSERT INTO sales_contracts(contract_date, customer_name, customer_email," +
                "vin, sales_tax, recording_fee, processing_fee, total_price, finance_option)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, salesContract.getContractDate());
            preparedStatement.setString(2,salesContract.getCustomerName());
            preparedStatement.setString(3, salesContract.getCustomerEmail());
            preparedStatement.setInt(4, salesContract.getVin());
            preparedStatement.setDouble(5, salesContract.getSalesTax());

            preparedStatement.setDouble(6, salesContract.getRecordingFee());
            preparedStatement.setDouble(7, salesContract.getProcessingFee());
            preparedStatement.setDouble(8, salesContract.getTotalPrice());
            preparedStatement.setString(9, salesContract.getFinanceOption());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0) {

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if(generatedKeys.next()) {

                    int contractId = generatedKeys.getInt(1);

                    return getBySalesContractId(contractId);
                }
            }

        } catch(SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    protected static SalesContract mapResultSet(ResultSet resultSet) throws SQLException {

        int contractId = resultSet.getInt("contract_id");
        String date = resultSet.getString("contract_date");
        String customerName = resultSet.getString("customer_name");
        String customerEmail = resultSet.getString("customer_email");
        int vin = resultSet.getInt("vin");
        double salesTax = resultSet.getDouble("sales_tax");
        double recordingFee = resultSet.getDouble("recording_fee");
        double processingFee = resultSet.getDouble("processing_fee");
        double totalPrice = resultSet.getDouble("total_price");
        String financeOption = resultSet.getString("finance_option");
        double monthlyPayment = resultSet.getDouble("monthly_payment");

        return new SalesContract(contractId, date, customerName, customerEmail, vin , salesTax,
                recordingFee, processingFee, totalPrice, financeOption, monthlyPayment);
    }
}
