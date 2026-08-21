package com.canaydin.mediconnect.patient.entity;

import com.canaydin.mediconnect.common.entity.BaseEntity;
import com.canaydin.mediconnect.doctor.entity.Doctor;
import com.canaydin.mediconnect.security.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patient_profiles")
public class PatientProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private UserAccount userAccount;

    @Column(
            name = "phone",
            length = 50
    )
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(
            name = "city",
            length = 100
    )
    private String city;

    @Column(
            name = "address",
            length = 500
    )
    private String address;

    @Column(
            name = "profile_image",
            columnDefinition = "bytea"
    )
    private byte[] profileImage;

    @Column(
            name = "profile_image_name",
            length = 255
    )
    private String profileImageName;

    @Column(
            name = "profile_image_type",
            length = 100
    )
    private String profileImageType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "patient_favorite_doctors",
            joinColumns = @JoinColumn(
                    name = "patient_profile_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "doctor_id"
            )
    )
    private Set<Doctor> favoriteDoctors = new HashSet<>();
}