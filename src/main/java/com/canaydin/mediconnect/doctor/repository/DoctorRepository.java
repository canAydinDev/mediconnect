package com.canaydin.mediconnect.doctor.repository;

import com.canaydin.mediconnect.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByClinicId(Long clinicId);

    List<Doctor> findBySpecialty(String specialty);

    List<Doctor> findByActiveTrue();

    List<Doctor> findByClinicIdAndActiveTrue(Long clinicId);

    @Query("""
           select d
           from Doctor d
           join fetch d.clinic
           """)
    List<Doctor> findAllWithClinic();

    @Query("""
           select d
           from Doctor d
           join fetch d.clinic
           where d.id = :id
           """)
    Optional<Doctor> findByIdWithClinic(@Param("id") Long id);

    @Query("""
           select d
           from Doctor d
           join fetch d.clinic
           where d.specialty = :specialty
           """)
    List<Doctor> findBySpecialtyWithClinic(@Param("specialty") String specialty);

    @Query("""
           select d
           from Doctor d
           join fetch d.clinic
           where d.active = true
           """)
    List<Doctor> findActiveDoctorsWithClinic();

    @Query("""
           select d
           from Doctor d
           join fetch d.clinic
           where d.clinic.id = :clinicId
           """)
    List<Doctor> findByClinicIdWithClinic(@Param("clinicId") Long clinicId);

    @Query("""
           select d
           from Doctor d
           join fetch d.clinic
           where d.clinic.id = :clinicId
           and d.active = true
           """)
    List<Doctor> findActiveDoctorsByClinicIdWithClinic(@Param("clinicId") Long clinicId);
}