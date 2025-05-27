package com.bogdan.loans.service.impl;

import com.bogdan.loans.constants.LoansConstants;
import com.bogdan.loans.dto.LoansDTO;
import com.bogdan.loans.entity.Loans;
import com.bogdan.loans.exception.LoanAlreadyExistsException;
import com.bogdan.loans.exception.ResourceNotFoundException;
import com.bogdan.loans.mapper.LoansMapper;
import com.bogdan.loans.repository.LoansRepository;
import com.bogdan.loans.service.ILoansService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
public class LoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    private MessageSource messageSource;

    public LoansServiceImpl(LoansRepository loansRepository, MessageSource messageSource) {
        this.loansRepository = loansRepository;
        this.messageSource = messageSource;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoans= loansRepository.findByMobileNumber(mobileNumber);

        if(optionalLoans.isPresent()) {
            throw new LoanAlreadyExistsException(messageSource.getMessage(
                    "exception.loan.create",
                    new String[]{mobileNumber},
                    Locale.getDefault()));
        }

        loansRepository.save(createNewLoan(mobileNumber));
    }

    /**
     *
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDTO getLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.loan.resource.not_found",
                        new String[]{"Loan", "mobile number", mobileNumber},
                        Locale.getDefault()))
        );

        return LoansMapper.mapToLoansDTO(loans, new LoansDTO());
    }

    /**
     *
     * @param loansDTO - LoansDto Object
     * @return boolean indicating if the update of loan details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDTO loansDTO) {
        boolean isUpdated = false;

        if (loansDTO != null) {
            Loans loans = loansRepository.findByLoanNumber(loansDTO.getLoanNumber()).orElseThrow(
                    () -> new ResourceNotFoundException(messageSource.getMessage(
                            "exception.loan.resource.not_found",
                            new String[]{"Loan", "loan number", loansDTO.getLoanNumber()},
                            Locale.getDefault()))
            );

            Optional<Loans> checkLoanMobile = loansRepository.findByMobileNumber(loansDTO.getMobileNumber());

            if (checkLoanMobile.isPresent() && !loans.getMobileNumber().equals(loansDTO.getMobileNumber())) {
                throw new LoanAlreadyExistsException(messageSource.getMessage(
                        "exception.loan.update.mobile_number",
                        new String[]{loansDTO.getMobileNumber()},
                        Locale.getDefault()));
            }

            LoansMapper.mapToLoans(loansDTO, loans);
            loansRepository.save(loans);

            isUpdated = true;
        }

        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input MobileNumber
     * @return boolean indicating if delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.loan.resource.not_found",
                        new String[]{"Loan", "mobile number", mobileNumber},
                        Locale.getDefault()))
        );

        loansRepository.deleteById(loans.getLoanId());

        return true;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
