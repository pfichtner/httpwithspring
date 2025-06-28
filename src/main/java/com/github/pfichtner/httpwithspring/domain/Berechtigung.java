package com.github.pfichtner.httpwithspring.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Berechtigung {

	BerechtigungsId id;
	String foo;
	int bar;
	String foobar;

}
