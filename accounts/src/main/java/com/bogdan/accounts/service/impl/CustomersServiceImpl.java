package com.bogdan.accounts.service.impl;

import com.bogdan.accounts.dto.AccountsDTO;
import com.bogdan.accounts.dto.CardsDTO;
import com.bogdan.accounts.dto.CustomerDetailsDTO;
import com.bogdan.accounts.dto.LoansDTO;
import com.bogdan.accounts.entity.Accounts;
import com.bogdan.accounts.entity.Customer;
import com.bogdan.accounts.exception.ResourceNotFoundException;
import com.bogdan.accounts.mapper.AccountsMapper;
import com.bogdan.accounts.mapper.CustomerMapper;
import com.bogdan.accounts.repository.AccountsRepository;
import com.bogdan.accounts.repository.CustomerRepository;
import com.bogdan.accounts.service.ICustomersService;
import com.bogdan.accounts.service.client.CardsFeignClient;
import com.bogdan.accounts.service.client.LoansFeignClient;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;

    private CustomerRepository customerRepository;

    private CardsFeignClient cardsFeignClient;

    private LoansFeignClient loansFeignClient;

    private MessageSource messageSource;

    public CustomersServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository,
                                CardsFeignClient cardsFeignClient, LoansFeignClient loansFeignClient, MessageSource messageSource) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.cardsFeignClient = cardsFeignClient;
        this.loansFeignClient = loansFeignClient;
        this.messageSource = messageSource;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDTO getCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.customer.resource.not_found",
                        new String[]{"Customer", "mobile number", mobileNumber},
                        Locale.getDefault()))
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.customer.resource.not_found",
                        new String[]{"Account", "customer id", customer.getCustomerId().toString()},
                        Locale.getDefault()))
        );

        CustomerDetailsDTO customerDetailsDTO = CustomerMapper.mapToCustomerDetailsDTO(customer, new CustomerDetailsDTO());
        customerDetailsDTO.setAccountsDTO(AccountsMapper.mapToAccountsDTO(accounts, new AccountsDTO()));

        ResponseEntity<LoansDTO> loansDtoResponseEntity = loansFeignClient.getLoanDetails(correlationId, mobileNumber);
        customerDetailsDTO.setLoansDTO(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDTO> cardsDtoResponseEntity = cardsFeignClient.getCardDetails(correlationId, mobileNumber);
        customerDetailsDTO.setCardsDTO(cardsDtoResponseEntity.getBody());

        return customerDetailsDTO;
    }

}
