package com.bogdan.accounts.mapper;

import com.bogdan.accounts.dto.AccountsDTO;
import com.bogdan.accounts.entity.Accounts;

public class AccountsMapper {

    public static AccountsDTO mapToAccountsDTO(Accounts accounts, AccountsDTO accountsDTO) {
        accountsDTO.setAccountType(accounts.getAccountType());
        accountsDTO.setBranchAddress(accounts.getBranchAddress());
        accountsDTO.setAccountNumber(accounts.getAccountNumber());
        return accountsDTO;
    }

    public static Accounts mapToAccounts(AccountsDTO accountsDTO, Accounts accounts) {
        accounts.setAccountType(accountsDTO.getAccountType());
        accounts.setBranchAddress(accountsDTO.getBranchAddress());
        accounts.setAccountNumber(accountsDTO.getAccountNumber());
        return accounts;
    }
}
