package com.alexandersaul.Alexander.controller;

import com.alexandersaul.Alexander.dto.ForumConfigCreateDto;
import com.alexandersaul.Alexander.dto.ForumConfigResponseDto;
import com.alexandersaul.Alexander.service.ForumConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/forum-config")
@RequiredArgsConstructor
public class ForumConfigController {

    private final ForumConfigService forumConfigService;

    // Crear un nuevo subreddit a monitorear
    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody ForumConfigCreateDto forumConfigCreateDto) {
        forumConfigService.create(forumConfigCreateDto);
        return ResponseEntity.ok("ForumConfig creado con éxito");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumConfigResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(forumConfigService.findById(id));
    }

    // Listar todos los subreddits configurados
    @GetMapping
    public ResponseEntity<List<ForumConfigResponseDto>> findAll() {
        List<ForumConfigResponseDto> configs = forumConfigService.findAll();
        return ResponseEntity.ok(configs);
    }

    // Eliminar un subreddit por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        forumConfigService.deleteById(id);
        return ResponseEntity.ok("ForumConfig eliminado con éxito");
    }

    @GetMapping("/by-start-date")
    public ResponseEntity<List<ForumConfigResponseDto>> findByStartDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date) {
        return ResponseEntity.ok(forumConfigService.findByStartDate(date));
    }

}
