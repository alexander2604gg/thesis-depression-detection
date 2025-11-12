package com.alexandersaul.Alexander.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Analysis")
@Data
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double depressionScore;

    @Column(columnDefinition = "TEXT")
    private String modelVersion;

    @Column
    private String label;

    @Column(nullable = false)
    private LocalDateTime analyzedAt = LocalDateTime.now();

    @OneToOne(mappedBy = "analysis")
    @JsonIgnore
    private Post post;
}
