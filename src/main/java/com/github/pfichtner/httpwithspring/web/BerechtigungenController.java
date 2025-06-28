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

import com.github.pfichtner.httpwithspring.domain.BerechtigungenService;
import com.github.pfichtner.httpwithspring.domain.BerechtigungsId;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/berechtigungen")
public class BerechtigungenController {

	private final BerechtigungenService service;

	@GetMapping("/{id}")
	public ResponseEntity<BerechtigungDTO> getBerechtigung(@PathVariable UUID id) {
		return ResponseEntity.of(service.load(new BerechtigungsId(id)).map(BerechtigungDTO::fromDomain));
	}

	@PutMapping("/{id}")
	public void putBerechtigung(@PathVariable UUID id, @RequestBody BerechtigungDTO berechtigung) {
		service.save(berechtigung.toDomain(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteBerechtigung(@PathVariable UUID id) {
		boolean deleted = service.delete(new BerechtigungsId(id));
		return (deleted ? noContent() : notFound()).build();
	}

}
