package com.testing.app.publisher.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.testing.app.publisher.model.MessageAction;
import com.testing.app.publisher.model.MessageEvent;
//import net.logstash.logback.argument.StructuredArguments;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import static net.logstash.logback.argument.StructuredArguments.*;

@Slf4j
@Service
public class MessagePublisherService {
  private final KafkaTemplate<String, MessageEvent> kafkaTemplate;

  private final String topic;

//  private static final Logger log = LoggerFactory.getLogger(MessagePublisherService.class);

  public MessagePublisherService(KafkaTemplate<String, MessageEvent> kafkaTemplate,
                                 @Value("${app.kafka.topic:testing-app-topic}") String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
  }

  public MessageEvent publishCreate(String payload) {
    String id = UUID.randomUUID().toString();
    MessageEvent event = new MessageEvent(id, MessageAction.CREATED, payload, Instant.now());
    log.info("Create message request received",
            keyValue("eventType", "CREATE_MESSAGE"),
            keyValue("eventId", id),
            keyValue("action", MessageAction.CREATED),
            value("event", event));
//    log.info("Mesaj publicat spre Kafka",
//            StructuredArguments.kv("topic", "testing-app-topic"),
//            StructuredArguments.kv("event_id", event.getId()),
//            StructuredArguments.kv("payload", event.getPayload())
//    );
    kafkaTemplate.send(topic, id, event);
    return event;
  }

  public MessageEvent publishUpdate(String id, String payload) {
    MessageEvent event = new MessageEvent(id, MessageAction.UPDATED, payload, Instant.now());
    kafkaTemplate.send(topic, id, event);
    return event;
  }

  public MessageEvent publishDelete(String id) {
    MessageEvent event = new MessageEvent(id, MessageAction.DELETED, null, Instant.now());
    kafkaTemplate.send(topic, id, event);
    return event;
  }
}
