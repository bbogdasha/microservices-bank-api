package com.bogdan.accounts.service.impl;

import com.bogdan.accounts.constants.AccountsConstants;
import com.bogdan.accounts.dto.AccountsDTO;
import com.bogdan.accounts.dto.CustomerDTO;
import com.bogdan.accounts.entity.Accounts;
import com.bogdan.accounts.entity.Customer;
import com.bogdan.accounts.exception.CustomerAlreadyExistsException;
import com.bogdan.accounts.exception.ResourceNotFoundException;
import com.bogdan.accounts.mapper.AccountsMapper;
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

        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * @param mobileNumber - input mobile number
     * @return Account details based on a given mn
     */
    @Override
    public CustomerDTO getAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.resource.not_found",
                        new String[]{"Customer", "mobile number", mobileNumber},
                        Locale.getDefault()))
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.resource.not_found",
                        new String[]{"Account", "customer id", customer.getCustomerId().toString()},
                        Locale.getDefault()))
        );

        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDTO(customer, new CustomerDTO());
        customerDTO.setAccountsDTO(AccountsMapper.mapToAccountsDTO(accounts, new AccountsDTO()));

        return customerDTO;
    }

    /**
     * @param customerDTO - customer object DTO
     * @return Account details is successful updated or not
     */
    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {
        boolean isUpdated = false;
        AccountsDTO accountsDTO = customerDTO.getAccountsDTO();

        if (accountsDTO != null) {
            Accounts accounts = accountsRepository.findById(accountsDTO.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException(messageSource.getMessage(
                            "exception.resource.not_found",
                            new String[]{"Account", "account number", accountsDTO.getAccountNumber().toString()},
                            Locale.getDefault()))
            );

            AccountsMapper.mapToAccounts(accountsDTO, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException(messageSource.getMessage(
                            "exception.resource.not_found",
                            new String[]{"Customer", "customer id", customerId.toString()},
                            Locale.getDefault()))
            );

            Optional<Customer> checkCustomerMobile = customerRepository.findByMobileNumber(customerDTO.getMobileNumber());

            if (checkCustomerMobile.isPresent() && !customer.getMobileNumber().equals(customerDTO.getMobileNumber())) {
                throw new CustomerAlreadyExistsException(messageSource.getMessage(
                        "exception.customer.update.mobile_number",
                        new String[]{customerDTO.getMobileNumber()},
                        Locale.getDefault()));
            }

            CustomerMapper.mapToCustomer(customerDTO, customer);
            customerRepository.save(customer);

            isUpdated = true;
        }

        return isUpdated;
    }

    /**
     * @param mobileNumber - input mobile number
     * @return Account is successful deleted or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException(messageSource.getMessage(
                        "exception.resource.not_found",
                        new String[]{"Customer", "mobile number", mobileNumber},
                        Locale.getDefault()))
        );

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());

        return true;
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

        return newAccount;
    }
}
