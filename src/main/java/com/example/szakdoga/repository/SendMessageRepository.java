package com.example.szakdoga.repository;

import com.example.szakdoga.model.SendMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendMessageRepository extends JpaRepository<SendMessage, Integer> {
    List<SendMessage> findAllBySenderUserIdAndReceiverUserId(Integer senderUserId, Integer receiverUserId);
}
