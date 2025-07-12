package com.github.pfichtner.httpwithspring.outbox.data;

import static java.time.temporal.ChronoUnit.MILLIS;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = PRIVATE)
@NoArgsConstructor(access = PUBLIC)
@Setter
@Accessors(chain = true)
public class OutboxEvent {

	@Id
	@GeneratedValue
	UUID id;
	final Instant createdAt = Instant.now().truncatedTo(MILLIS);
	@Column(nullable = false)
	boolean published;
	String aggregateType;
	String aggregateId;
	String type; // event type
	String payload; // JSON

}
