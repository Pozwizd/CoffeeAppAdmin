package com.spacelab.coffeeapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String email;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private double bonusPoints = 0;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Order> orders = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Language language;
//    @ManyToOne
//    @JoinColumn(name = "invitation_id")
//    private Invitation invitation;
    @Column(name = "registration_date", columnDefinition="DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
    @OneToOne(mappedBy = "customer")
    private PasswordResetTokenCustomer passwordResetTokenCustomer;
    private boolean deleted = false;
}