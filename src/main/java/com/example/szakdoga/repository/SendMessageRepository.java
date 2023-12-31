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

    @Query("SELECT sm FROM SendMessage sm WHERE sm.message_id IN (SELECT MAX(s.message_id) FROM SendMessage s WHERE s.receiverUser.id = :receiverId GROUP BY s.senderUser.id) ORDER BY sm.message_id DESC")
    List<SendMessage> findAllLatestMessagesByReceiverUserId(@Param("receiverId") Integer receiverId);

    @Query("SELECT m FROM SendMessage m WHERE (m.senderUser.id = :senderId AND m.receiverUser.id = :receiverId) OR (m.senderUser.id = :receiverId AND m.receiverUser.id = :senderId)")
    List<SendMessage> findMessagesBetweenUsers(Integer senderId, Integer receiverId);
}
