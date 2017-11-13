package com.solofeed.genesis.user.domain;

import com.solofeed.genesis.user.dao.converter.UserRoleConverter;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    /** Generated user id */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** User's name */
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /** User's email address */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /** User's password */
    @Column(name = "password", nullable = false)
    private String password;

    /** User's role */
    @Column(name = "role", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private Role role;

    /**
     * Represents a state at a T instant for a user. When the email, role or password are changed, this state
     * must be changed to revoke token access
     */
    @Column(name = "state")
    private String state;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        setState(UUID.randomUUID().toString());
    }
}
