package rw.gov.erp.v1.services.mail.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.services.mail.MailService;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
  private final JavaMailSender mailSender;
  // private final TemplateEngine templateEngine;

  @Async
  @Override
  public void sendMail(String to, String subject, String message) {

    SimpleMailMessage mailMessage = new SimpleMailMessage();

    mailMessage.setTo(to);
    mailMessage.setSubject(subject);
    mailMessage.setText(message);

    mailSender.send(mailMessage);
  }

}
