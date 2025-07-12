package com.github.pfichtner.httpwithspring.outbox;

import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;

public interface MessagePublisher {
	void publish(OutboxEvent event);
}
