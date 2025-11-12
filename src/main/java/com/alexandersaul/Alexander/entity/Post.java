package com.alexandersaul.Alexander.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Post")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String redditId; // ID del post en Reddit (p.e. t3_abc123)

    @Column(nullable = true)
    private String author; // nombre de usuario del autor en Reddit

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    @Column(nullable = false)
    private boolean messageSent = false;

    @Column(nullable = false)
    private LocalDateTime createdAtReddit;

    @Column(nullable = false)
    private LocalDateTime fetchedAt = LocalDateTime.now();

    @ManyToOne(optional = true)
    @JoinColumn(name = "lot_id", nullable = true)
    private Lot lot;

    // Relación sin cascada
    @OneToOne
    @JoinColumn(name = "analysis_id", referencedColumnName = "id")
    private Analysis analysis;
}
