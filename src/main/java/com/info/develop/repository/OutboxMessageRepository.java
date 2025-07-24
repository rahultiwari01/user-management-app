package com.info.develop.repository;

import com.info.develop.model.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {
}