package com.canaydin.mediconnect.contact.service.impl;

import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;
import com.canaydin.mediconnect.contact.entity.ContactMessage;
import com.canaydin.mediconnect.contact.enums.ContactMessageStatus;
import com.canaydin.mediconnect.contact.repository.ContactMessageRepository;
import com.canaydin.mediconnect.contact.service.ContactMessageService;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;


    @Override
    public ContactMessageResponse findContactMessageById(Long id) {
        ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact Message", "id", id));
        return transformContactMessage(contactMessage);
    }

    @Override
    public List<ContactMessageResponse> findContactMessageByStatus(String status) {
        ContactMessageStatus contactMessageStatus = ContactMessageStatus.valueOf(status);

        return contactMessageRepository.findByStatus(contactMessageStatus).
                stream().map(this::transformContactMessage).toList();
    }

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

    @Override
    public List<ContactMessageResponse> findAllContactMessages() {
        List<ContactMessage> allContactMessages = contactMessageRepository.findAll();
       return allContactMessages.stream().map(this::transformContactMessage).toList();
    }


    @Override
    public void deleteContactMessage(Long id) {
        ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact Message", "id", id));
        contactMessageRepository.delete(contactMessage);

    }

    @Override
    public ContactMessageResponse updateStatus(Long id, String status) {
        ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact Message", "id", id));
        contactMessage.setStatus(ContactMessageStatus.valueOf(status));
        contactMessageRepository.save(contactMessage);
        return transformContactMessage(contactMessage);

    }

    private ContactMessageResponse transformContactMessage(ContactMessage contactMessage) {
        return new ContactMessageResponse(
                contactMessage.getId(),
                contactMessage.getFullName(),
                contactMessage.getEmail(),
                contactMessage.getUserType(),
                contactMessage.getSubject(),
                contactMessage.getMessage(),
                contactMessage.getStatus().name(),
                contactMessage.getCreatedAt()
        );
    }
}
