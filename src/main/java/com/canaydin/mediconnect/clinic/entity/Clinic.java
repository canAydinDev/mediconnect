package com.canaydin.mediconnect.clinic.entity;

import com.canaydin.mediconnect.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

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



}
