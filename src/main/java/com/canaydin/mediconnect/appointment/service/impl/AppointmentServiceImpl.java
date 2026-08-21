package com.canaydin.mediconnect.appointment.service.impl;

import com.canaydin.mediconnect.appointment.dto.AppointmentDto;
import com.canaydin.mediconnect.appointment.dto.BookAppointmentRequestDto;
import com.canaydin.mediconnect.appointment.entity.Appointment;
import com.canaydin.mediconnect.appointment.enums.AppointmentStatus;
import com.canaydin.mediconnect.appointment.repository.AppointmentRepository;
import com.canaydin.mediconnect.appointment.service.AppointmentService;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserAccountRepository userAccountRepository;


    // =====================================================
    // BOOK APPOINTMENT
    // =====================================================

    @Override
    @Transactional
    public AppointmentDto bookMyAppointment(
            BookAppointmentRequestDto request,
            String patientEmail
    ) {

        UserAccount patient =
                getPatientByEmail(patientEmail);

        Doctor doctor =
                doctorRepository
                        .findActiveDoctorByIdWithClinic(
                                request.doctorId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor",
                                        "id",
                                        request.doctorId()
                                )
                        );

        // Doctor aktif olsa bile bağlı olduğu clinic inactive olabilir.
        if (doctor.getClinic().getStatus() != ClinicStatus.ACTIVE) {

            throw new BusinessConflictException(
                    "Appointment cannot be created for an inactive clinic"
            );
        }

        /*
         * Bu statuslara sahip randevular ilgili zamanı dolu kabul ettirir.
         *
         * PENDING   -> slot dolu
         * CONFIRMED -> slot dolu
         *
         * CANCELLED -> slot tekrar kullanılabilir
         * COMPLETED -> aktif slot olarak değerlendirilmez
         */
        Set<AppointmentStatus> blockingStatuses =
                Set.of(
                        AppointmentStatus.PENDING,
                        AppointmentStatus.CONFIRMED
                );


        // =================================================
        // CHECK DOCTOR AVAILABILITY
        // =================================================

        boolean doctorBusy =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentAtAndStatusIn(
                                doctor.getId(),
                                request.appointmentAt(),
                                blockingStatuses
                        );

        if (doctorBusy) {

            throw new BusinessConflictException(
                    "The doctor already has an appointment at this time"
            );
        }


        // =================================================
        // CHECK PATIENT AVAILABILITY
        // =================================================

        boolean patientBusy =
                appointmentRepository
                        .existsByPatientIdAndAppointmentAtAndStatusIn(
                                patient.getId(),
                                request.appointmentAt(),
                                blockingStatuses
                        );

        if (patientBusy) {

            throw new BusinessConflictException(
                    "You already have an appointment at this time"
            );
        }


        // =================================================
        // CREATE APPOINTMENT
        // =================================================

        Appointment appointment =
                new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentAt(
                request.appointmentAt()
        );
        appointment.setReason(
                request.reason()
        );
        appointment.setStatus(
                AppointmentStatus.PENDING
        );

        /*
         * appointment new ile oluşturulduğu için TRANSIENT durumda.
         *
         * Bu nedenle save() burada gereklidir.
         */
        Appointment savedAppointment =
                appointmentRepository.save(
                        appointment
                );

        return mapToDto(savedAppointment);
    }


    // =====================================================
    // CANCEL APPOINTMENT
    // =====================================================

    @Override
    @Transactional
    public AppointmentDto cancelMyAppointment(
            Long appointmentId,
            String patientEmail
    ) {

        UserAccount patient =
                getPatientByEmail(patientEmail);

        /*
         * Sadece appointmentId ile aramıyoruz.
         *
         * Böylece login olan patient başka bir patient'a
         * ait appointment'ı cancel edemez.
         *
         * appointment.id = appointmentId
         * AND
         * appointment.patient.id = patient.id
         */
        Appointment appointment =
                appointmentRepository
                        .findByIdAndPatientIdWithDoctorAndClinic(
                                appointmentId,
                                patient.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment",
                                        "id",
                                        appointmentId
                                )
                        );


        // Tamamlanmış randevu artık iptal edilemez.
        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            throw new BusinessConflictException(
                    "Completed appointment cannot be cancelled"
            );
        }


        /*
         * Zaten CANCELLED ise tekrar hata vermiyoruz.
         *
         * Böylece cancel operasyonu idempotent davranıyor.
         */
        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            return mapToDto(appointment);
        }


        /*
         * Appointment DB'den yüklendiği için MANAGED durumda.
         *
         * save() çağırmamıza gerek yok.
         * Transaction sonunda Hibernate dirty checking ile
         * UPDATE işlemini gerçekleştirir.
         */
        appointment.setStatus(
                AppointmentStatus.CANCELLED
        );

        return mapToDto(appointment);
    }


    // =====================================================
    // GET MY APPOINTMENTS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getMyAppointments(
            String patientEmail
    ) {

        UserAccount patient =
                getPatientByEmail(patientEmail);

        return appointmentRepository
                .findByPatientIdWithDoctorAndClinic(
                        patient.getId()
                )
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    // =====================================================
    // GET PATIENT BY EMAIL
    // =====================================================

    private UserAccount getPatientByEmail(
            String patientEmail
    ) {

        String normalizedEmail =
                patientEmail
                        .trim()
                        .toLowerCase(Locale.ROOT);

        UserAccount patient =
                userAccountRepository
                        .findByEmail(normalizedEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User",
                                        "email",
                                        normalizedEmail
                                )
                        );


        /*
         * SecurityConfig zaten /api/patients/me/**
         * endpointlerini PATIENT rolüne sınırlandırıyor.
         *
         * Buna rağmen service katmanında da ek
         * business kontrolü bırakıyoruz.
         */
        if (patient.getRole() != Role.PATIENT) {

            throw new BusinessConflictException(
                    "User must have PATIENT role"
            );
        }


        if (!patient.isActive()) {

            throw new BusinessConflictException(
                    "Patient account is inactive"
            );
        }

        return patient;
    }


    // =====================================================
    // ENTITY -> DTO
    // =====================================================

    private AppointmentDto mapToDto(
            Appointment appointment
    ) {

        Doctor doctor =
                appointment.getDoctor();

        return new AppointmentDto(

                appointment.getId(),

                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpecialty(),

                doctor.getClinic().getId(),
                doctor.getClinic().getName(),

                appointment.getAppointmentAt(),
                appointment.getStatus(),
                appointment.getReason(),

                appointment.getCreatedAt(),
                appointment.getUpdatedAt()
        );
    }
}