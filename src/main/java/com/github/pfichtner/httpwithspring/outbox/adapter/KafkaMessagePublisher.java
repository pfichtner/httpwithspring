
package com.github.pfichtner.httpwithspring.outbox.adapter;

import static java.lang.String.format;

import org.springframework.stereotype.Service;

import com.github.pfichtner.httpwithspring.outbox.MessagePublisher;
import com.github.pfichtner.httpwithspring.outbox.data.OutboxEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher implements MessagePublisher {

//    private final KafkaTemplate<String, String> kafkaTemplate;

	@Override
	public void publish(OutboxEvent event) {
		String topic = resolveTopic(event.getType());
//        kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload());
		System.out.println(format("kafkaTemplate.send(%s, %s, %s);", topic, event.getAggregateId(), event.getPayload()));
//        
	}

	private String resolveTopic(String eventType) {
		// You can use logic or a map to route different event types to different topics
		return "my-entity-events";
	}
}
