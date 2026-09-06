package com.canaydin.mediconnect.appointment.service.impl;

import com.canaydin.mediconnect.appointment.dto.ClinicAdminAppointmentDto;
import com.canaydin.mediconnect.appointment.dto.ClinicAdminAppointmentUpdateRequestDto;
import com.canaydin.mediconnect.appointment.entity.Appointment;
import com.canaydin.mediconnect.appointment.enums.AppointmentStatus;
import com.canaydin.mediconnect.appointment.repository.AppointmentRepository;
import com.canaydin.mediconnect.appointment.service.ClinicAdminAppointmentService;
import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.enums.ClinicStatus;
import com.canaydin.mediconnect.doctor.entity.Doctor;
import com.canaydin.mediconnect.doctor.repository.DoctorRepository;
import com.canaydin.mediconnect.exception.BusinessConflictException;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import com.canaydin.mediconnect.security.user.enums.Role;
import com.canaydin.mediconnect.security.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ClinicAdminAppointmentServiceImpl
        implements ClinicAdminAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserAccountRepository userAccountRepository;


    // =====================================================
    // GET DOCTOR APPOINTMENTS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ClinicAdminAppointmentDto> getDoctorAppointments(
            Long doctorId,
            String clinicAdminEmail
    ) {

        Clinic clinic =
                getAssignedClinicForClinicAdmin(
                        clinicAdminEmail
                );

        /*
         * Doctor gerçekten bu Clinic Admin'in
         * clinic'ine mi ait?
         */
        doctorRepository
                .findByIdAndClinicIdWithClinic(
                        doctorId,
                        clinic.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor",
                                "id",
                                doctorId
                        )
                );

        return appointmentRepository
                .findByDoctorIdAndClinicIdForClinicAdmin(
                        doctorId,
                        clinic.getId()
                )
                .stream()
                .map(this::mapToClinicAdminDto)
                .toList();
    }


    // =====================================================
    // UPDATE APPOINTMENT
    // =====================================================

    @Override
    @Transactional
    public ClinicAdminAppointmentDto updateAppointment(
            Long appointmentId,
            ClinicAdminAppointmentUpdateRequestDto request,
            String clinicAdminEmail
    ) {

        Clinic clinic =
                getAssignedClinicForClinicAdmin(
                        clinicAdminEmail
                );

        Appointment appointment =
                appointmentRepository
                        .findByIdAndClinicIdForClinicAdmin(
                                appointmentId,
                                clinic.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment",
                                        "id",
                                        appointmentId
                                )
                        );

        AppointmentStatus currentStatus =
                appointment.getStatus();

        AppointmentStatus newStatus =
                request.status();


        /*
         * Aynı status tekrar gönderilmişse hata vermiyoruz.
         * Internal note yine güncellenebilir.
         */
        if (currentStatus != newStatus) {

            validateStatusTransition(
                    currentStatus,
                    newStatus
            );

            appointment.setStatus(
                    newStatus
            );
        }

        appointment.setInternalNotes(
                request.internalNotes()
        );

        /*
         * save() YOK.
         *
         * appointment DB'den yüklendi ve managed.
         * @Transactional sonunda dirty checking çalışır.
         */
        return mapToClinicAdminDto(
                appointment
        );
    }


    // =====================================================
    // STATUS TRANSITION VALIDATION
    // =====================================================

    private void validateStatusTransition(
            AppointmentStatus currentStatus,
            AppointmentStatus newStatus
    ) {

        boolean validTransition =
                switch (currentStatus) {

                    case PENDING -> newStatus == AppointmentStatus.CONFIRMED
                            || newStatus == AppointmentStatus.REJECTED;

                    case CONFIRMED -> newStatus == AppointmentStatus.COMPLETED
                            || newStatus == AppointmentStatus.CANCELLED;

                    case REJECTED,
                         CANCELLED,
                         COMPLETED -> false;
                };

        if (!validTransition) {

            throw new BusinessConflictException(
                    "Invalid appointment status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }
    }


    // =====================================================
    // GET CLINIC ADMIN'S CLINIC
    // =====================================================

    private Clinic getAssignedClinicForClinicAdmin(
            String clinicAdminEmail
    ) {

        String normalizedEmail =
                clinicAdminEmail
                        .trim()
                        .toLowerCase(Locale.ROOT);

        UserAccount userAccount =
                userAccountRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User",
                                        "email",
                                        normalizedEmail
                                )
                        );

        if (userAccount.getRole()
                != Role.CLINIC_ADMIN) {

            throw new BusinessConflictException(
                    "User must have CLINIC_ADMIN role"
            );
        }

        if (!userAccount.isActive()) {

            throw new BusinessConflictException(
                    "Clinic admin account is inactive"
            );
        }

        Clinic clinic =
                userAccount.getClinic();

        if (clinic == null) {

            throw new BusinessConflictException(
                    "Clinic admin is not assigned to a clinic"
            );
        }

        if (clinic.getStatus()
                != ClinicStatus.ACTIVE) {

            throw new BusinessConflictException(
                    "Assigned clinic is inactive"
            );
        }

        return clinic;
    }


    // =====================================================
    // ENTITY -> DTO
    // =====================================================

    private ClinicAdminAppointmentDto mapToClinicAdminDto(
            Appointment appointment
    ) {

        UserAccount patient =
                appointment.getPatient();

        Doctor doctor =
                appointment.getDoctor();

        Clinic clinic =
                doctor.getClinic();

        return new ClinicAdminAppointmentDto(

                appointment.getId(),

                patient.getId(),
                patient.getFullName(),
                patient.getEmail(),

                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpecialty(),

                clinic.getId(),
                clinic.getName(),

                appointment.getAppointmentAt(),
                appointment.getStatus(),

                appointment.getReason(),
                appointment.getInternalNotes(),

                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}