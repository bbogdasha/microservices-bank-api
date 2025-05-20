package com.bogdan.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Schema(name = "Account details",
        description = "Schema to hold Account details")
public class AccountsDTO {

    @Schema(description = "Account number of customer in bank.", example = "1234567890")
    @NotEmpty(message = "Account number can not be a null or empty.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits.")
    private Long accountNumber;

    @Schema(description = "Account type of of customer bank account.", example = "Savings")
    @NotEmpty(message = "Account type can not be a null or empty.")
    private String accountType;

    @Schema(description = "Account branch address.", example = "New York 123")
    @NotEmpty(message = "Branch address can not be a null or empty.")
    private String branchAddress;

    //Getter, Setter, ToString

    public AccountsDTO(Long accountNumber, String accountType, String branchAddress) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.branchAddress = branchAddress;
    }

    public AccountsDTO() {
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    @Override
    public String toString() {
        return "AccountsDTO{" +
                "accountNumber=" + accountNumber +
                ", accountType='" + accountType + '\'' +
                ", branchAddress='" + branchAddress + '\'' +
                '}';
    }
}
