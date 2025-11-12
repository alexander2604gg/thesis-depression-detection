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
@Table(name = "lot")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "forum_id", nullable = true)
    private ForumConfig forumConfig;

    @OneToMany(mappedBy = "lot")
    @JsonIgnore // 👈 evita que se serialice "lots"
    private List<Post> posts;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
