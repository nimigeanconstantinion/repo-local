package com.testing.app.consumer.service;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testing.app.consumer.model.MessageAction;
import com.testing.app.consumer.model.MessageEntity;
import com.testing.app.consumer.model.MessageEvent;
import com.testing.app.consumer.repo.MessageRepository;

@Service
public class MessageConsumerService {
  private final MessageRepository repository;

  public MessageConsumerService(MessageRepository repository) {
    this.repository = repository;
  }

  @Transactional
  @KafkaListener(topics = "${app.kafka.topic}")
  public void handle(MessageEvent event) {
    System.out.println("Kafka consumer received: " + event);
    if (event == null || event.getId() == null) {
      return;
    }
    if (MessageAction.DELETED.equals(event.getAction())) {
      repository.deleteById(event.getId());
      return;
    }
    MessageEntity entity = new MessageEntity(
        event.getId(),
        event.getAction().name(),
        event.getPayload(),
        event.getTimestamp() != null ? event.getTimestamp() : Instant.now());
    repository.save(entity);
  }
}
