package com.github.pfichtner.httpwithspring.web;

import static lombok.AccessLevel.PUBLIC;

import java.util.UUID;

import com.github.pfichtner.httpwithspring.domain.Berechtigung;
import com.github.pfichtner.httpwithspring.domain.BerechtigungsId;

import lombok.Builder;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PUBLIC, makeFinal = true)
@Builder(toBuilder = true)
public class BerechtigungDTO {

	String foo;
	Integer bar;
	String foobar;

	public static BerechtigungDTO fromDomain(Berechtigung domain) {
		return BerechtigungDTO.builder() //
				.foo(domain.getFoo()) //
				.bar(domain.getBar()) //
				.foobar(domain.getFoobar()) //
				.build();
	}

	public Berechtigung toDomain(UUID id) {
		return Berechtigung.builder() //
				.id(new BerechtigungsId(id)) //
				.foo(foo) //
				.bar(bar == null ? 0 : bar) //
				.foobar(foobar) //
				.build();
	}

}
