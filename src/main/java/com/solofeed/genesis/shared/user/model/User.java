package com.solofeed.genesis.shared.user.model;

import com.solofeed.genesis.shared.user.constant.UserRoleEnum;
import com.solofeed.genesis.shared.user.converter.UserRoleConverter;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRoleEnum role;
}
