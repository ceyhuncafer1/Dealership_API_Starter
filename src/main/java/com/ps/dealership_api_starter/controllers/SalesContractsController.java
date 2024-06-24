package com.ps.dealership_api_starter.controllers;

import com.ps.dealership_api_starter.data.SalesContractsDao;
import com.ps.dealership_api_starter.models.SalesContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/sales_contracts")
@CrossOrigin

public class SalesContractsController {

    private SalesContractsDao salesContractsDao;

    @Autowired
    public SalesContractsController(SalesContractsDao salesContractsDao) {
        this.salesContractsDao = salesContractsDao;
    }

    @GetMapping("{id}")
    public SalesContract getBySalesId(@PathVariable int id) {

        try {
            SalesContract salesContract = salesContractsDao.getBySalesContractId(id);

            if(salesContract == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return salesContract;

        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    public SalesContract addSalesContract(@RequestBody SalesContract salesContract) {

        try {

            return salesContractsDao.createSalesContract(salesContract);

        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

}