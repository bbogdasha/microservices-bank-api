package com.bogdan.accounts.service.impl;

import com.bogdan.accounts.constants.AccountsConstants;
import com.bogdan.accounts.dto.CustomerDTO;
import com.bogdan.accounts.entity.Accounts;
import com.bogdan.accounts.entity.Customer;
import com.bogdan.accounts.exception.CustomerAlreadyExistsException;
import com.bogdan.accounts.mapper.CustomerMapper;
import com.bogdan.accounts.repository.AccountsRepository;
import com.bogdan.accounts.repository.CustomerRepository;
import com.bogdan.accounts.service.IAccountsService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;

    private CustomerRepository customerRepository;

    private MessageSource messageSource;

    public AccountsServiceImpl(AccountsRepository accountsRepository,
                               CustomerRepository customerRepository,
                               MessageSource messageSource) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
        this.messageSource = messageSource;
    }

    /**
     * @param customerDTO - customer object DTO
     */
    @Override
    public void createAccount(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDTO.getMobileNumber());

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException(messageSource.getMessage(
                    "exception.customer.create",
                    new String[]{customerDTO.getMobileNumber()},
                    Locale.getDefault()));
        }

        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("ADMIN");

        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param customer - Customer object
     * @return the new Account info
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("ADMIN");

        return newAccount;
    }
}
