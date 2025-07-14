package com.github.pfichtner.httpwithspring.domain;

import static lombok.AccessLevel.PRIVATE;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = PRIVATE)
public class Berechtigung {

	@Id
	UUID id;
	String foo;
	Integer bar;
	String foobar;

	@Getter(onMethod = @__(@DomainEvents))
	@JsonIgnore
	private transient final List<Object> events = new ArrayList<>();

	public void registerEvent(Object event) {
		events.add(event);
	}

	@AfterDomainEventPublication
	public void clearEvents() {
		events.clear();
	}

}
