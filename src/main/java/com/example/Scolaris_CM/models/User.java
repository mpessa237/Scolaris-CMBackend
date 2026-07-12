package com.example.Scolaris_CM.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    private boolean active = true;

    // Notes saisies par cet utilisateur (si role = TEACHER)
    @OneToMany(mappedBy = "recordedBy")
    private List<Grade> grades;

    // Absences enregistrées par cet utilisateur (si role = SUPERVISOR)
    @OneToMany(mappedBy = "recordedBy")
    private List<Absence> absences;

    // Sanctions enregistrées par cet utilisateur (si role = SUPERVISOR)
    @OneToMany(mappedBy = "recordedBy")
    private List<Sanction> sanctions;

    // Créneaux enseignés par cet utilisateur (si role = TEACHER)
    @OneToMany(mappedBy = "teacher")
    private List<Timetable> timetableSlots;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
