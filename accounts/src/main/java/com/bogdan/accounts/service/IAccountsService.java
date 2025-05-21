package com.bogdan.accounts.service;

import com.bogdan.accounts.dto.CustomerDTO;

public interface IAccountsService {

    /**
     * @param customerDTO - customer object DTO
     */
    void createAccount(CustomerDTO customerDTO);

    /**
     * @param mobileNumber - input mobile number
     * @return Account details based on a given mn
     */
    CustomerDTO getAccount(String mobileNumber);

    /**
     * @param customerDTO - customer object DTO
     * @return Account details is successful updated or not
     */
    boolean updateAccount(CustomerDTO customerDTO);

    /**
     * @param mobileNumber - input mobile number
     * @return Account is successful deleted or not
     */
    boolean deleteAccount(String mobileNumber);
}
