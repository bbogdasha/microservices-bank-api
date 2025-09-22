package com.bogdan.accounts.service;

import com.bogdan.accounts.dto.CustomerDetailsDTO;

public interface ICustomersService {

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    CustomerDetailsDTO getCustomerDetails(String mobileNumber, String correlationId);

}
