package com.alexandersaul.Alexander.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor // <-- necesario para Hibernate
@AllArgsConstructor // opcional, si quieres constructor con todos los campos
@Entity
@Table(name = "forum_config")
public class ForumConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String subreddit;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime startDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime endDate;

    private float interval;
    // Relación sin cascada
    @OneToMany(mappedBy = "forumConfig")
    @JsonIgnore // 👈 así no muestra toda la config
    private List<Lot> lots;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserSec user;

}
