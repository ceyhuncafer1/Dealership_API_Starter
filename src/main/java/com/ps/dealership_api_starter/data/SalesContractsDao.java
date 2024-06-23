package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.SalesContract;

public interface SalesContractsDao {

    SalesContract getBySalesContractId(int contract_id);
    SalesContract createSalesContract(SalesContract salesContract);

}