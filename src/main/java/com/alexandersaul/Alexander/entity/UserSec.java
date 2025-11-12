package com.alexandersaul.Alexander.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private Boolean enabled;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}