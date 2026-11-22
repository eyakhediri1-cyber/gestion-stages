package tn.gestionstages.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    private String university;

    private String specialization;

    private String studyLevel;

    private String phone;

    private String cvUrl;

    private String linkedinUrl;

    private String githubUrl;
}
