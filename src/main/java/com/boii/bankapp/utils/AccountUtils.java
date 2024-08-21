package com.boii.bankapp.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE ="001";
    public static final String ACCOUNT_EXISTS_MESSAGE ="This User has already exists.";
    public static final String ACCOUNT_CREATION_SUCCESS ="002";
    public static final String ACCOUNT_CREATION_MESSAGE ="Account Successfully created.";
    public static final String ACCOUNT_NOT_EXISTS_CODE ="003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE ="This User does not exist.";
    public static final String ACCOUNT_FOUND_CODE ="004";
    public static final String ACCOUNT_FOUND_MESSAGE ="User Found.";
    public static final String ACCOUNT_CREDIT_CODE_SUCCESS ="005";
    public static final String ACCOUNT_CREDIT_MESSAGE_SUCCESS ="Credit Successfully Completed.";
    public static final String INSUFFICIENT_FUND_CODE ="006";
    public static final String INSUFFICIENT_FUND_MESSAGE ="Insufficient Funds.";
    public static final String ACCOUNT_DEBIT_CODE_SUCCESS ="007";
    public static final String ACCOUNT_DEBIT_MESSAGE_SUCCESS ="Debit Successfully.";
    public static final String ACCOUNT_DOES_NOT_EXISTS_MESSAGE ="Account Does not exists.";
    public static final String TRANSFER_SUCCESSFUL_CODE="008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE ="Transfer Successfully Completed.";



    public static String generateAccountNumber() {
        /*
         * Account number = 2024 + randomEightDigits
         */

        Year currentYear = Year.now();
        int min = 10000000;
        int max = 99999999;

        // generate random number between min and max

        int randNumber = (int)Math.floor(Math.random() *(max - min + 1) + min);
        // Convert the Current and random number to String , then Concatenate with currYear

        String year = String.valueOf(currentYear);

        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();
    }
}
