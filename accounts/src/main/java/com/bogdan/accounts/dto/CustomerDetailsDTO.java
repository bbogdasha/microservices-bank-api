package com.bogdan.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "CustomerDetails",
        description = "Schema to hold Customer, Account, Cards and Loans information.")
public class CustomerDetailsDTO {

    @Schema(description = "Name of the customer", example = "Eazy Bytes")
    @NotEmpty(message = "Name can not be a null or empty")
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")
    private String name;

    @Schema(description = "Email address of the customer", example = "tutor@eazybytes.com")
    @NotEmpty(message = "Email address can not be a null or empty")
    @Email(message = "Email address should be a valid value")
    private String email;

    @Schema(description = "Mobile Number of the customer", example = "9345432123")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(description = "Account details of the Customer")
    private AccountsDTO accountsDTO;

    @Schema(description = "Loans details of the Customer")
    private LoansDTO loansDTO;

    @Schema(description = "Cards details of the Customer")
    private CardsDTO cardsDTO;

    //Getter, Setter, ToString


    public CustomerDetailsDTO(String name, String email, String mobileNumber, AccountsDTO accountsDTO, LoansDTO loansDTO, CardsDTO cardsDTO) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.accountsDTO = accountsDTO;
        this.loansDTO = loansDTO;
        this.cardsDTO = cardsDTO;
    }

    public CustomerDetailsDTO() {
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

    public LoansDTO getLoansDTO() {
        return loansDTO;
    }

    public void setLoansDTO(LoansDTO loansDTO) {
        this.loansDTO = loansDTO;
    }

    public CardsDTO getCardsDTO() {
        return cardsDTO;
    }

    public void setCardsDTO(CardsDTO cardsDTO) {
        this.cardsDTO = cardsDTO;
    }

    @Override
    public String toString() {
        return "CustomerDetailsDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", accountsDTO=" + accountsDTO +
                ", loansDTO=" + loansDTO +
                ", cardsDTO=" + cardsDTO +
                '}';
    }
}
