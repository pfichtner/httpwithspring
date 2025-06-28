package com.github.pfichtner.httpwithspring.data;

import static lombok.AccessLevel.PUBLIC;

import java.util.UUID;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.domain.BerechtigungsId;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = PUBLIC)
@NoArgsConstructor(access = PUBLIC) // needed by JPA
@AllArgsConstructor
@Builder(toBuilder = true)
public class BerechtigungEntity {

	@Id
	UUID id;
	String foo;
	int bar;
	String foobar;

	public static BerechtigungEntity fromDomain(Berechtigung domain) {
		return BerechtigungEntity.builder() //
				.id(domain.getId().value()) //
				.foo(domain.getFoo()) //
				.bar(domain.getBar()) //
				.foobar(domain.getFoobar()) //
				.build();
	}

	public Berechtigung toDomain() {
		return Berechtigung.builder() //
				.id(new BerechtigungsId(id)) //
				.foo(foo) //
				.bar(bar) //
				.foobar(foobar) //
				.build();
	}

}
