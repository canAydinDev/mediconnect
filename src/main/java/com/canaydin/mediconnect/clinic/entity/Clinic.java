package com.canaydin.mediconnect.clinic.entity;

import com.canaydin.mediconnect.common.entity.BaseEntity;
import com.canaydin.mediconnect.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "CLINICS")
@Getter
@Setter
@NoArgsConstructor
public class Clinic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "LOGO_URL", length = 255)
    private String logoUrl;

    @Column(name = "CATEGORY", length = 100)
    private String category;

    @Column(name = "CITY", length = 100)
    private String city;

    @Column(name = "ADDRESS", length = 255)
    private String address;

    @Column(name = "PHONE", length = 50)
    private String phone;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "RATING", precision = 2, scale = 1)
    private BigDecimal rating;


    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "clinic")
    private List<Doctor> doctors;



}
