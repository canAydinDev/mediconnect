package com.canaydin.mediconnect.doctor.repository;

import com.canaydin.mediconnect.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByClinicId(Long clinicId);

    List<Doctor> findBySpecialty(String specialty);

    List<Doctor> findByActiveTrue();

    List<Doctor> findByClinicIdAndActiveTrue(Long clinicId);
}