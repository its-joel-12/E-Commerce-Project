package com.joel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_name"),
        @UniqueConstraint(columnNames = "user_email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(max = 20)
    private String userName;

    @Email
    @NotBlank
    @Size(max = 50)
    private String userEmail;

    @NotBlank
    @Size(max = 120)
    private String userPassword;

    public User(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}
