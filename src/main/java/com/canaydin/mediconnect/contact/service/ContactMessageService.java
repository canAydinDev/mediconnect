package com.canaydin.mediconnect.contact.service;

import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;

public interface ContactMessageService {
ContactMessageResponse createContactMessage(ContactMessageRequest contactMessageRequest);
}
