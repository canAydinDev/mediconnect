package com.canaydin.mediconnect.doctor.entity;

import com.canaydin.mediconnect.clinic.entity.Clinic;
import com.canaydin.mediconnect.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "specialty", nullable = false, length = 100)
    private String specialty;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "active")
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;
}