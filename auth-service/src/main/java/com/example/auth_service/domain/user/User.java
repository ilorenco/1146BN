package com.example.auth_service.domain.user;

import com.example.auth_service.domain.user.vo.Email;
import com.example.auth_service.domain.user.vo.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Table
@Entity
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Email email;

    @Embedded
    private Role role;
}
