package com.nova.guestApp.service;

import com.nova.guestApp.dtos.request.EmailDetailsRequest;

public interface EmailService {
    void sendEmailAlert(EmailDetailsRequest request);
}
