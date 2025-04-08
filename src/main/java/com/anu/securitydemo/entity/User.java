package com.anu.securitydemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    private long id;
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
