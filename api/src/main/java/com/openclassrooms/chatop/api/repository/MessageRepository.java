package com.openclassrooms.chatop.api.repository;

import com.openclassrooms.chatop.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Message entity.
 * Provides CRUD operations for message management.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
