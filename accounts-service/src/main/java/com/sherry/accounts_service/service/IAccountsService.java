package com.sherry.accounts_service.service;


import com.sherry.accounts_service.dto.CustomerDto;
import com.sherry.accounts_service.entity.Customer;

public interface IAccountsService {

    void createAccount(CustomerDto customerDto);
    boolean updateAccount(CustomerDto customerDto);
    boolean deleteAccount(String mobileNumber);


}
