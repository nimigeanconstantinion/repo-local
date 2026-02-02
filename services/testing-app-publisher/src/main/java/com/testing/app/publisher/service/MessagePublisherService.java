package com.testing.app.publisher.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.testing.app.publisher.model.MessageAction;
import com.testing.app.publisher.model.MessageEvent;

@Service
public class MessagePublisherService {
  private final KafkaTemplate<String, MessageEvent> kafkaTemplate;
  private final String topic;

  public MessagePublisherService(KafkaTemplate<String, MessageEvent> kafkaTemplate,
      @Value("${app.kafka.topic}") String topic) {
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
  }

  public MessageEvent publishCreate(String payload) {
    String id = UUID.randomUUID().toString();
    MessageEvent event = new MessageEvent(id, MessageAction.CREATED, payload, Instant.now());
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
