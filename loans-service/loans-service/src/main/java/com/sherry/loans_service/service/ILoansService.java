package com.sherry.loans_service.service;


import com.sherry.loans_service.dto.LoansDto;

public interface ILoansService {


    void createLoan(String mobileNumber);


    LoansDto fetchLoan(String mobileNumber);


    boolean updateLoan(LoansDto loansDto);

    boolean deleteLoan(String mobileNumber);

}
