package com.testing.app.publisher.api;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.testing.app.publisher.model.MessageEvent;
import com.testing.app.publisher.service.MessagePublisherService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
  private final MessagePublisherService publisherService;

  public MessageController(MessagePublisherService publisherService) {
    this.publisherService = publisherService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MessageEvent create(@Valid @RequestBody MessageRequest request) {
    return publisherService.publishCreate(request.getPayload());
  }

  @PutMapping("/{id}")
  public MessageEvent update(@PathVariable String id, @Valid @RequestBody MessageRequest request) {
    return publisherService.publishUpdate(id, request.getPayload());
  }

  @DeleteMapping("/{id}")
  public MessageEvent delete(@PathVariable String id) {
    return publisherService.publishDelete(id);
  }
}
