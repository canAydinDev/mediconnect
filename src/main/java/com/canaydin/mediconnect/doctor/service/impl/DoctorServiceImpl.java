package com.canaydin.mediconnect.doctor.service.impl;

import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.clinic.repository.ClinicRepository;
import com.canaydin.mediconnect.doctor.dto.DoctorDto;
import com.canaydin.mediconnect.doctor.dto.DoctorRequestDto;
import com.canaydin.mediconnect.doctor.entity.Doctor;
import com.canaydin.mediconnect.doctor.repository.DoctorRepository;
import com.canaydin.mediconnect.doctor.service.DoctorService;
import com.canaydin.mediconnect.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor


public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;

    @Override
    @Transactional
    public DoctorDto createDoctor(DoctorRequestDto doctorRequestDto) {
        Clinic clinic = clinicRepository.findById(doctorRequestDto.clinicId()).orElseThrow(()-> new ResourceNotFoundException("Clinic", "id", doctorRequestDto.clinicId()));
        Doctor doctor = new Doctor();
        doctor.setFullName(doctorRequestDto.fullName());
        doctor.setEmail(doctorRequestDto.email());
        doctor.setPhone(doctorRequestDto.phone());
        doctor.setTitle(doctorRequestDto.title());
        doctor.setSpecialty(doctorRequestDto.specialty());
        doctor.setBio(doctorRequestDto.bio());
        doctor.setImageUrl(doctorRequestDto.imageUrl());
        doctor.setExperienceYears(doctorRequestDto.experienceYears());
        doctor.setActive(doctorRequestDto.active() != null ? doctorRequestDto.active() : true);
        doctor.setClinic(clinic);

        return mapToDoctorDto(doctorRepository.save(doctor));

    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(Long id, DoctorRequestDto doctorRequestDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        Clinic clinic = clinicRepository.findById(doctorRequestDto.clinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic", "id", doctorRequestDto.clinicId()));

        doctor.setFullName(doctorRequestDto.fullName());
        doctor.setEmail(doctorRequestDto.email());
        doctor.setPhone(doctorRequestDto.phone());
        doctor.setTitle(doctorRequestDto.title());
        doctor.setSpecialty(doctorRequestDto.specialty());
        doctor.setBio(doctorRequestDto.bio());
        doctor.setImageUrl(doctorRequestDto.imageUrl());
        doctor.setExperienceYears(doctorRequestDto.experienceYears());
        doctor.setActive(doctorRequestDto.active() != null ? doctorRequestDto.active() : true);
        doctor.setClinic(clinic);

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return mapToDoctorDto(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        doctorRepository.delete(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {

       Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return mapToDoctorDto(doctor);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getDoctorsByClinicId(Long clinicId) {
        return doctorRepository.findByClinicId(clinicId)
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty)
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getActiveDoctors() {
        return doctorRepository.findByActiveTrue()
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDto> getActiveDoctorsByClinicId(Long clinicId) {
        return doctorRepository.findByClinicIdAndActiveTrue(clinicId)
                .stream()
                .map(this::mapToDoctorDto)
                .toList();
    }

    private DoctorDto mapToDoctorDto(Doctor doctor) {
        return new DoctorDto(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getTitle(),
                doctor.getSpecialty(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.getBio(),
                doctor.getImageUrl(),
                doctor.getExperienceYears(),
                doctor.getActive(),
                doctor.getClinic().getId(),
                doctor.getClinic().getName(),
                doctor.getCreatedAt()
        );
    }
}
