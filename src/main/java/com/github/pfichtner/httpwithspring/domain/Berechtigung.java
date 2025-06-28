package com.github.pfichtner.httpwithspring.domain;

import static lombok.AccessLevel.PRIVATE;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = PRIVATE)
public class Berechtigung {

	@Id
	@JsonIgnore
	UUID id;
	String foo;
	Integer bar;
	String foobar;

}
