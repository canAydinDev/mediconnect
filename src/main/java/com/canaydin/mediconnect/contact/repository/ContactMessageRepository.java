package com.canaydin.mediconnect.contact.repository;

import com.canaydin.mediconnect.contact.entity.ContactMessage;
import com.canaydin.mediconnect.contact.enums.ContactMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

        List<ContactMessage> findByStatus(ContactMessageStatus status);
}
