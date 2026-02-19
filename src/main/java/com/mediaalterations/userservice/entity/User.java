package com.mediaalterations.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 150)
    private String fullname;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /*
        @PrePersist is a JPA lifecycle callback that executes before the entity is persisted, allowing custom logic before insert. It is vendor-independent.
        @CreationTimestamp is a Hibernate-specific annotation that automatically populates the field during insert. It is simpler but less flexible.
     */
//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDateTime.now();
//    }

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private UserProfile userProfile;
}
