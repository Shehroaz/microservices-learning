package com.sherry.accounts_service.service.impl;

import com.sherry.accounts_service.constants.AccountsConstants;
import com.sherry.accounts_service.dto.CustomerDto;

import com.sherry.accounts_service.entity.Accounts;
import com.sherry.accounts_service.entity.Customer;
import com.sherry.accounts_service.exception.CustomerAlreadyExistsException;
import com.sherry.accounts_service.mapper.CustomerMapper;
import com.sherry.accounts_service.repository.AccountsRepository;
import com.sherry.accounts_service.repository.CustomerRepository;
import com.sherry.accounts_service.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static com.sherry.accounts_service.constants.AccountsConstants.ADDRESS;
import static com.sherry.accounts_service.constants.AccountsConstants.SAVINGS;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {
    private final CustomerRepository customerRepository;
    private final AccountsRepository accountsRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = this.customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists with mobileNumber: " + customerDto.getMobileNumber());
        }
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = this.customerRepository.save(customer);
        Accounts newAccount = createNewAccount(savedCustomer);
        this.accountsRepository.save(newAccount);
    }

    private Accounts createNewAccount(Customer customer){
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        accounts.setAccountNumber(randomAccNumber);
        accounts.setAccountType(SAVINGS);
        accounts.setBranchAddress(ADDRESS);
        return accounts;

    }
}
