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
import org.springframework.web.bind.annotation.RestController;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.domain.BerechtigungenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BerechtigungenController {

	private final BerechtigungenService service;

	@GetMapping("/berechtigungen/{uuid}")
	public ResponseEntity<Berechtigung> getBerechtigung(@PathVariable UUID uuid) {
		return ResponseEntity.of(service.load(uuid));
	}

	@PutMapping("/berechtigungen/{id}")
	public void putBerechtigung(@PathVariable UUID id, @RequestBody Berechtigung berechtigung) {
		berechtigung.setId(id);
		service.save(berechtigung);
	}

	@DeleteMapping("/berechtigungen/{id}")
	public ResponseEntity<Object> deleteBerechtigung(@PathVariable UUID id) {
		boolean deleted = service.delete(id);
		return (deleted ? noContent() : notFound()).build();
	}

}
