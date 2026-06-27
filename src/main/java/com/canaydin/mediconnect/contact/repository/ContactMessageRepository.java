package com.canaydin.mediconnect.contact.repository;

import com.canaydin.mediconnect.contact.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findByStatus(String status);
}
