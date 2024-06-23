package com.ps.dealership_api_starter.data;

import com.ps.dealership_api_starter.models.LeaseContract;

public interface LeaseContractsDao {

    LeaseContract getByLeaseContractId(int contract_id);
    LeaseContract addLeaseContract(LeaseContract leaseContract);

}
