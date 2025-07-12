package com.github.pfichtner.httpwithspring.outbox.data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

	List<OutboxEvent> findByPublishedFalseOrderByCreatedAtAsc();

	default List<OutboxEvent> findUnpublished() {
		return findByPublishedFalseOrderByCreatedAtAsc();
	}

	void deleteByPublishedIsTrueAndCreatedAtBefore(Instant cutoff);

}
