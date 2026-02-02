package com.testing.app.consumer.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.testing.app.consumer.model.MessageEntity;
import com.testing.app.consumer.repo.MessageRepository;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
  private final MessageRepository repository;

  public MessageController(MessageRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<MessageEntity> all() {
    return repository.findAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MessageEntity byId(@PathVariable String id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));
  }
}
