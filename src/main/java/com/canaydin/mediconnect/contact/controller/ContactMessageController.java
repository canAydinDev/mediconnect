package com.canaydin.mediconnect.contact.controller;

import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;
import com.canaydin.mediconnect.contact.service.ContactMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contact-messages")
@RequiredArgsConstructor
@Validated
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @GetMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<ContactMessageResponse> findContactMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(contactMessageService.findContactMessageById(id));
    }

    @GetMapping(value = "/by-status", version = "1.0")
    public ResponseEntity<List<ContactMessageResponse>> findContactMessageByStatus(@RequestParam
                                                                                       @NotBlank(message = "Status cannot be blank")
                                                                                       @Pattern(
                                                                                               regexp = "NEW|IN_PROGRESS|RESOLVED|CLOSED",
                                                                                               message = "Status must be one of NEW, IN_PROGRESS, RESOLVED, CLOSED"
                                                                                       )
                                                                                       String status  ) {
        return ResponseEntity.ok(contactMessageService.findContactMessageByStatus(status));
    }

    @GetMapping( version = "1.0")
    public ResponseEntity<List<ContactMessageResponse>> findAllContactMessages() {
        return  ResponseEntity.ok(contactMessageService.findAllContactMessages());
    }

    @PostMapping(version = "1.0")
    public ResponseEntity<ContactMessageResponse> createContactMessage (@Valid @RequestBody ContactMessageRequest contactMessageRequest){
        ContactMessageResponse contactMessageResponse = contactMessageService.createContactMessage(contactMessageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactMessageResponse);
    }

    @PutMapping(value = "/{id}/status", version = "1.0")
    public ResponseEntity<ContactMessageResponse> updateContactMessageStatus(
            @PathVariable Long id,
            @RequestParam
            @NotBlank(message = "Status cannot be blank")
            @Pattern(
                    regexp = "NEW|IN_PROGRESS|RESOLVED|CLOSED",
                    message = "Status must be one of NEW, IN_PROGRESS, RESOLVED, CLOSED"
            )
            String status
    ) {
        return ResponseEntity.ok(contactMessageService.updateStatus(id, status));
    }

    @DeleteMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<Void> deleteContactMessageById(@PathVariable Long id){
        contactMessageService.deleteContactMessage(id);
        return ResponseEntity.noContent().build();
    }
}
