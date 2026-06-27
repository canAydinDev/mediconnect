package com.canaydin.mediconnect.contact.service;

import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;

import java.util.List;

public interface ContactMessageService {

    ContactMessageResponse findContactMessageById(Long id);

    List<ContactMessageResponse> findContactMessageByStatus(String status);

    ContactMessageResponse createContactMessage(ContactMessageRequest contactMessageRequest);

    List<ContactMessageResponse> findAllContactMessages();

    void deleteContactMessage(Long id);

    ContactMessageResponse updateStatus(Long id, String status);

}


