package rw.gov.erp.v1.services.message;

import java.util.List;
import java.util.UUID;

import rw.gov.erp.v1.entities.message.Message;

public interface MessageService {

    public List<Message> getEmployeeMessages(UUID employeeId);

    public List<Message> getMessagesByMonthYear(String monthYear);
}
