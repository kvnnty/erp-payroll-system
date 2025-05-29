package rw.gov.erp.v1.repositories.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rw.gov.erp.v1.entities.message.Message;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByEmployeeId(UUID employeeId);

  List<Message> findByMonthYear(String monthYear);
}
