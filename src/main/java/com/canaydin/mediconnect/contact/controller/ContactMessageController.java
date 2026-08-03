package com.canaydin.mediconnect.contact.controller;

import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;
import com.canaydin.mediconnect.contact.service.ContactMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/contact-messages")
@RequiredArgsConstructor
@Validated
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping(version = "1.0")
    public ResponseEntity<ContactMessageResponse> createContactMessage(
            @Valid @RequestBody ContactMessageRequest contactMessageRequest
    ) {
        ContactMessageResponse contactMessageResponse =
                contactMessageService.createContactMessage(contactMessageRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(contactMessageResponse);
    }


    @GetMapping(value = "/admin/{id}", version = "1.0")
    public ResponseEntity<ContactMessageResponse> findContactMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(contactMessageService.findContactMessageById(id));
    }


    @GetMapping(value = "/admin/by-status", version = "1.0")
    public ResponseEntity<Page<ContactMessageResponse>> findContactMessageByStatus(
            @RequestParam
            @NotBlank(message = "Status cannot be blank")
            @Pattern(
                    regexp = "NEW|IN_PROGRESS|RESOLVED|CLOSED",
                    message = "Status must be one of NEW, IN_PROGRESS, RESOLVED, CLOSED"
            )
            String status,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size must be at most 100")
            int size,

            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            @Pattern(
                    regexp = "(?i)asc|desc",
                    message = "Direction must be asc or desc"
            )
            String direction
    ) {
        return ResponseEntity.ok(
                contactMessageService.findContactMessageByStatus(
                        status,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @PatchMapping(value = "/admin/{id}/status", version = "1.0")
    public ResponseEntity<Void> updateContactMessageStatus(
            @PathVariable Long id,

            @RequestParam
            @NotBlank(message = "Status cannot be blank")
            @Pattern(
                    regexp = "NEW|IN_PROGRESS|RESOLVED|CLOSED",
                    message = "Status must be one of NEW, IN_PROGRESS, RESOLVED, CLOSED"
            )
            String status
    ) {
        contactMessageService.updateStatus(id, status);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/admin/{id}", version = "1.0")
    public ResponseEntity<Void> deleteContactMessageById(@PathVariable Long id) {
        contactMessageService.deleteContactMessage(id);
        return ResponseEntity.noContent().build();
    }
}