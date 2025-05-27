package com.bogdan.loans.service;

import com.bogdan.loans.dto.LoansDTO;

public interface ILoansService {

    /**
     *
     * @param mobileNumber - Mobile Number of the Customer
     */
    void createLoan(String mobileNumber);

    /**
     *
     * @param mobileNumber - Input mobile Number
     *  @return Loan Details based on a given mobileNumber
     */
    LoansDTO getLoan(String mobileNumber);

    /**
     *
     * @param loansDTO - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateLoan(LoansDTO loansDTO);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if delete of loan details is successful or not
     */
    boolean deleteLoan(String mobileNumber);
}
