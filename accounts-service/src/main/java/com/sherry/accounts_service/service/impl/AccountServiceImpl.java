package com.sherry.accounts_service.service.impl;

import com.sherry.accounts_service.dto.AccountsDto;
import com.sherry.accounts_service.dto.CustomerDto;

import com.sherry.accounts_service.entity.Accounts;
import com.sherry.accounts_service.entity.Customer;
import com.sherry.accounts_service.exception.CustomerAlreadyExistsException;
import com.sherry.accounts_service.exception.ResourceNotFoundException;
import com.sherry.accounts_service.mapper.AccountsMapper;
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

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = this.accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(() ->
                    new ResourceNotFoundException("Account", "accountNumber", accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            this.accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
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
