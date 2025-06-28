package com.github.pfichtner.httpwithspring.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record BerechtigungsId(@Column(name = "id") UUID value) {
}
