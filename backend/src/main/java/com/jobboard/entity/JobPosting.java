package com.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_postings", indexes = {
    @Index(name = "idx_city", columnList = "city"),
    @Index(name = "idx_is_remote", columnList = "is_remote"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor

public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "company_id", nullable = false)
private Company company;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be less than 150 characters")
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    private String city;

    @Column(name = "is_remote")
    private Boolean isRemote = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "position_type", length = 50)
    private PositionType positionType;

    private String salary;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum PositionType {
        FULL_TIME,
        PART_TIME,
        INTERNSHIP,
        CONTRACT,
        FREELANCE
    }
}