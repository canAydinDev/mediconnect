package com.canaydin.mediconnect.contact.controller;


import com.canaydin.mediconnect.contact.dto.ContactMessageRequest;
import com.canaydin.mediconnect.contact.dto.ContactMessageResponse;
import com.canaydin.mediconnect.contact.service.ContactMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ContactMessageResponse> createContactMessage (@Valid @RequestBody ContactMessageRequest contactMessageRequest){
        ContactMessageResponse contactMessageResponse = contactMessageService.createContactMessage(contactMessageRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(contactMessageResponse);
    }

    @GetMapping(version = "1.0")
    public ResponseEntity<String> fetchContactMessagesByStatus(@RequestParam
                                                                   @NotBlank(message = "Status can not be blank")
                                                               @Size(min = 3, message = "Status length should be minimum 3 characters ")
                                                               String status){
        return ResponseEntity.ok("These are the contact messages with status: " + status);
    }
}
