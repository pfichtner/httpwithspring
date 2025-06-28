package com.github.pfichtner.httpwithspring.web;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pfichtner.httpwithspring.data.BerechtigungenJpaRepository;
import com.github.pfichtner.httpwithspring.domain.Berechtigung;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/berechtigungen")
public class BerechtigungenController {

	private final BerechtigungenJpaRepository repo;

	@GetMapping("/{id}")
	public ResponseEntity<Berechtigung> getBerechtigung(@PathVariable UUID id) {
		return ResponseEntity.of(repo.findById(id));
	}

	@PutMapping("/{id}")
	public void putBerechtigung(@PathVariable UUID id, @RequestBody Berechtigung berechtigung) {
		berechtigung.setId(id);
		repo.save(berechtigung);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteBerechtigung(@PathVariable UUID id) {
		boolean wasPresent = repo.findById(id).isPresent();
		repo.deleteById(id);
		return (wasPresent ? noContent() : notFound()).build();
	}

}
