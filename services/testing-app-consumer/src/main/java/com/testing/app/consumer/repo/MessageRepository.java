package com.testing.app.consumer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testing.app.consumer.model.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
}
