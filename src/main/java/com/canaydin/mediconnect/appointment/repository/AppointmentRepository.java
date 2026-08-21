package com.canaydin.mediconnect.appointment.repository;

import com.canaydin.mediconnect.appointment.entity.Appointment;
import com.canaydin.mediconnect.appointment.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentAtAndStatusIn(
            Long doctorId,
            Instant appointmentAt,
            Collection<AppointmentStatus> statuses
    );

    boolean existsByPatientIdAndAppointmentAtAndStatusIn(
            Long patientId,
            Instant appointmentAt,
            Collection<AppointmentStatus> statuses
    );

    @Query("""
            select a
            from Appointment a
            join fetch a.doctor d
            join fetch d.clinic
            where a.id = :appointmentId
              and a.patient.id = :patientId
            """)
    Optional<Appointment> findByIdAndPatientIdWithDoctorAndClinic(
            @Param("appointmentId")
            Long appointmentId,

            @Param("patientId")
            Long patientId
    );

    @Query("""
            select a
            from Appointment a
            join fetch a.doctor d
            join fetch d.clinic
            where a.patient.id = :patientId
            order by a.appointmentAt desc
            """)
    List<Appointment> findByPatientIdWithDoctorAndClinic(
            @Param("patientId")
            Long patientId
    );
}