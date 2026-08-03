package com.canaydin.mediconnect.contact.service.impl;

import com.canaydin.mediconnect.audit.AuditorAwareImpl;
import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;
import com.canaydin.mediconnect.contact.entity.ContactMessage;
import com.canaydin.mediconnect.contact.enums.ContactMessageStatus;
import com.canaydin.mediconnect.contact.repository.ContactMessageRepository;
import com.canaydin.mediconnect.contact.service.ContactMessageService;
import com.canaydin.mediconnect.exception.InvalidEnumValueException;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final AuditorAwareImpl auditorAware;


    @Override
    public ContactMessageResponse findContactMessageById(Long id) {
        ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact Message", "id", id));
        return transformContactMessage(contactMessage);
    }


    @Override
    public Page<ContactMessageResponse> findContactMessageByStatus(
            String status,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        ContactMessageStatus contactMessageStatus =
                ContactMessageStatus.valueOf(status.trim().toUpperCase());

        Sort sort = buildSort(sortBy, direction);

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        PageRequest pageRequest = PageRequest.of(safePage, safeSize, sort);

        return contactMessageRepository.findByStatus(contactMessageStatus, pageRequest)
                .map(this::transformContactMessage);
    }

    @Override
    @Transactional
    public ContactMessageResponse createContactMessage(ContactMessageRequest contactMessageRequest) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setFullName(contactMessageRequest.fullName());
        contactMessage.setEmail(contactMessageRequest.email());
        contactMessage.setUserType(contactMessageRequest.userType());
        contactMessage.setSubject(contactMessageRequest.subject());
        contactMessage.setMessage(contactMessageRequest.message());

        ContactMessage savedContactMessage = contactMessageRepository.save(contactMessage);

        return transformContactMessage(savedContactMessage);
    }


    @Override
    @Transactional
    public void deleteContactMessage(Long id) {
        ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact Message", "id", id));
        contactMessageRepository.delete(contactMessage);

    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        ContactMessageStatus newStatus;

        try {
            newStatus = ContactMessageStatus.valueOf(
                    status.trim().toUpperCase()
            );
        } catch (IllegalArgumentException exception) {
            throw new InvalidEnumValueException("status", status);
        }

        String updatedBy = auditorAware
                .getCurrentAuditor()
                .orElse("AnonymousUser");

        int updatedRows = contactMessageRepository.updateStatusById(
                id,
                newStatus,
                Instant.now(),
                updatedBy
        );

        if (updatedRows == 0) {
            throw new ResourceNotFoundException(
                    "Contact Message",
                    "id",
                    id
            );
        }
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

    private Sort buildSort(String sortBy, String direction) {
        List<String> allowedSortFields = List.of(
                "id",
                "fullName",
                "email",
                "userType",
                "subject",
                "status",
                "createdAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        return direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }
}
