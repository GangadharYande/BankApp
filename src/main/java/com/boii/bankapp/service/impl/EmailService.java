package com.boii.bankapp.service.impl;

import com.boii.bankapp.dto.EmailDetails;

public interface EmailService {


    void senderEmailAlert(EmailDetails emailDetails);

    void sendEmailWithAttactment(EmailDetails emailDetails);
}
