package com.canaydin.mediconnect.contact.entity;

import com.canaydin.mediconnect.common.entity.BaseEntity;
import com.canaydin.mediconnect.contact.enums.ContactMessageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact_messages")
public class ContactMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;

    @Column(name = "subject", nullable = false, length = 100)
    private String subject;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ContactMessageStatus status = ContactMessageStatus.NEW;
}