package com.canaydin.mediconnect.appointment.entity;

import com.canaydin.mediconnect.appointment.enums.AppointmentStatus;
import com.canaydin.mediconnect.common.entity.BaseEntity;
import com.canaydin.mediconnect.doctor.entity.Doctor;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "patient_id",
            nullable = false
    )
    private UserAccount patient;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "doctor_id",
            nullable = false
    )
    private Doctor doctor;

    @Column(
            name = "appointment_at",
            nullable = false
    )
    private Instant appointmentAt;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private AppointmentStatus status =
            AppointmentStatus.PENDING;

    @Column(
            name = "reason",
            columnDefinition = "text"
    )
    private String reason;
}