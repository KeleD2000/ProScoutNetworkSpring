package com.example.szakdoga.repository;

import com.example.szakdoga.model.SendMessage;
import com.example.szakdoga.model.dto.ReceiverUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendMessageRepository extends JpaRepository<SendMessage, Integer> {
    List<SendMessage> findAllBySenderUserIdAndReceiverUserId(Integer senderUserId, Integer receiverUserId);

    @Query("SELECT s.message_content FROM SendMessage s WHERE s.receiverUser.id = :id ORDER BY s.message_id DESC LIMIT 1")
    String msgReceiver(@Param("id") Integer id);

}
