package com.github.pfichtner.httpwithspring.web;

import static org.springframework.http.ResponseEntity.created;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.domain.BerechtigungenService;
import com.github.pfichtner.httpwithspring.domain.BerechtigungsId;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/berechtigungen")
class BerechtigungenController {

	private final BerechtigungenService service;

	@GetMapping("/{id}")
	public ResponseEntity<Berechtigung> getBerechtigung(@PathVariable UUID id) {
		return ResponseEntity.of(service.load(new BerechtigungsId(id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> putBerechtigung(@PathVariable UUID id, @RequestBody Berechtigung berechtigung) {
		berechtigung.setId(new BerechtigungsId(id));
		return service.save(berechtigung)
				? created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri()).build()
				: noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteBerechtigung(@PathVariable UUID id) {
		return (service.delete(new BerechtigungsId(id)) //
				? noContent() //
				: notFound()).build();
	}

}
