package com.mikepn.banking.services;

import com.mikepn.banking.enums.template.EmailTemplate;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;


public interface EmailService {
   void sendEmail(String to,
                  String username,
                  EmailTemplate emailTemplate,
                  String confirmationUrl,
                  String activationCode,
                  String subject
   ) throws MessagingException;
}
