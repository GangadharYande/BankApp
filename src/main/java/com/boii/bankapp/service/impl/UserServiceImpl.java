package com.boii.bankapp.service.impl;

import com.boii.bankapp.dto.*;
import com.boii.bankapp.entity.User;
import com.boii.bankapp.repository.UserRepository;
import com.boii.bankapp.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.math.BigDecimal;
import java.math.BigInteger;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService  transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /*  Creating an account - saving a new user in to DB
        *  Check if user already have account  - email
        * */

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .middleName(userRequest.getMiddleName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .State(userRequest.getState())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .phoneNumber((userRequest.getPhoneNumber()))
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .status("Active")
                .build();

        User savedUser = userRepository.save(newUser);


        // send email sent alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipients(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation  Your account has been created \n Your Account Details: \n "+
                "Account Name : " + savedUser.getFirstName()+" "+savedUser.getMiddleName()+ " "+ savedUser.getLastName()+
                        "\nAccount Number : "  + savedUser.getAccountNumber())
                .build();

        emailService.senderEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " +savedUser.getMiddleName() +" "+  savedUser.getLastName() )
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        // Check if provided account number exits in db
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+ foundUser.getMiddleName()+" "+ foundUser.getLastName())
                        .build()).build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXISTS_CODE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getMiddleName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // check if account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExists){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        // account exists pass
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        // Save Transaction
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDTO);


        return  BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_CODE_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_MESSAGE_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getMiddleName()
                        + " " + userToCredit.getLastName())
                        .accountNumber(request.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())

                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {

        // Check if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Fetch the user and available balance from the database
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        // Check if the available balance is sufficient
        if (availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUND_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }




        else {
            // Perform the debit operation
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDTO);

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_CODE_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_MESSAGE_SUCCESS)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getMiddleName()
                                    + " " + userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }


    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        // get account to debit (check if account exits)
        // check  currentAccount balance
        // debit the form this account
        // get account to credit
        //  credit to the account

        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DOES_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_FUND_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_FUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));

        String sourceUserName = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getMiddleName()+" " +
                sourceAccountUser.getLastName();

        userRepository.save(sourceAccountUser);

        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipients(sourceAccountUser.getEmail())
                .messageBody("The Sum of "+ request.getAmount() +" has been deducted from your account! Your Current Balance is "
                        + sourceAccountUser.getAccountBalance())
                .build();

        emailService.senderEmailAlert(debitAlert);

        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));

//        String recipientUsername = destinationAccountUser.getFirstName() +" " +
//                destinationAccountUser.getMiddleName()+" " +destinationAccountUser.getLastName() ;

        userRepository.save(destinationAccountUser);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipients(sourceAccountUser.getEmail())
                .messageBody("The Sum of "+ request.getAmount() +" has been Credited from your account! form "
                        + sourceAccountUser +" Your Current Balance is "
                        + sourceAccountUser.getAccountBalance())
                .build();

        emailService.senderEmailAlert(creditAlert);

        TransactionDTO transactionDTO = TransactionDTO.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("TRANSFER")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransaction(transactionDTO);
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }

}
