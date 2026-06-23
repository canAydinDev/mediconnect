package com.canaydin.mediconnect.contact.service.impl;

import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;
import com.canaydin.mediconnect.contact.entity.ContactMessage;
import com.canaydin.mediconnect.contact.repository.ContactMessageRepository;
import com.canaydin.mediconnect.contact.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;


    @Override
    public ContactMessageResponse createContactMessage(ContactMessageRequest contactMessageRequest) {
        ContactMessage  contactMessage = new ContactMessage();
        contactMessage.setFullName(contactMessageRequest.fullName());
        contactMessage.setEmail(contactMessageRequest.email());
        contactMessage.setUserType(contactMessageRequest.userType());
        contactMessage.setSubject(contactMessageRequest.subject());
        contactMessage.setMessage(contactMessageRequest.message());

        ContactMessage savedContactMessage = contactMessageRepository.save(contactMessage);

        return transformContactMessage(savedContactMessage);
    }

    private ContactMessageResponse transformContactMessage(ContactMessage contactMessage) {
        return new ContactMessageResponse(
                contactMessage.getId(),
                contactMessage.getFullName(),
                contactMessage.getEmail(),
                contactMessage.getUserType(),
                contactMessage.getSubject(),
                contactMessage.getMessage(),
                contactMessage.getStatus(),
                contactMessage.getCreatedAt()
        );
    }
}
