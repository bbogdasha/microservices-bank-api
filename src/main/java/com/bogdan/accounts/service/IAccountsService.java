package com.bogdan.accounts.service;

import com.bogdan.accounts.dto.CustomerDTO;

public interface IAccountsService {

    /**
     * @param customerDTO - customer object DTO
     */
    void createAccount(CustomerDTO customerDTO);
}
