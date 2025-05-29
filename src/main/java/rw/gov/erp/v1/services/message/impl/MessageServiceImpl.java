package rw.gov.erp.v1.services.message.impl;

import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.entities.message.Message;
import rw.gov.erp.v1.repositories.message.MessageRepository;
import rw.gov.erp.v1.services.message.MessageService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  public List<Message> getEmployeeMessages(UUID employeeId) {
    return messageRepository.findByEmployeeId(employeeId);
  }

  public List<Message> getMessagesByMonthYear(String monthYear) {
    return messageRepository.findByMonthYear(monthYear);
  }
}
