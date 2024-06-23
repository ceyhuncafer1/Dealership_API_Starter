package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.LeaseContractsDao;
import com.ps.dealership_api_starter.models.LeaseContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/lease_contracts")
@CrossOrigin

public class LeaseContractsController {

    private LeaseContractsDao leaseContractsDao;

    @Autowired
    public LeaseContractsController(LeaseContractsDao leaseContractsDao) {
        this.leaseContractsDao = leaseContractsDao;
    }

    @GetMapping("{id}")
    public LeaseContract getByLeaseId(@PathVariable int id) {

        try {

            LeaseContract leaseContract = leaseContractsDao.getByLeaseContractId(id);

            if(leaseContract == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return leaseContract;
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    public LeaseContract addLeaseContract(@RequestBody LeaseContract leaseContract) {

        try {

            return leaseContractsDao.addLeaseContract(leaseContract);

        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

}
