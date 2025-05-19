package com.bogdan.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerDTO {

    @NotEmpty(message = "Name can not be a null or empty.")
    @Size(min = 2, max = 54, message = "The length of the customer name should be between 2 and 54 symbols.")
    private String name;

    @NotEmpty(message = "Email can not be a null or empty.")
    @Email(message = "Email should be a valid value.")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits.")
    private String mobileNumber;

    private AccountsDTO accountsDTO;

    //Getter, Setter, ToString

    public CustomerDTO(String name, String email, String mobileNumber, AccountsDTO accountsDTO) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.accountsDTO = accountsDTO;
    }

    public CustomerDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public AccountsDTO getAccountsDTO() {
        return accountsDTO;
    }

    public void setAccountsDTO(AccountsDTO accountsDTO) {
        this.accountsDTO = accountsDTO;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", accountsDTO=" + accountsDTO +
                '}';
    }
}
