package com.example.szakdoga.services;

import com.example.szakdoga.model.*;
import com.example.szakdoga.model.dto.MessagesDto;
import com.example.szakdoga.model.dto.ReceiverAllDto;
import com.example.szakdoga.model.dto.ReceiverUserDto;
import com.example.szakdoga.model.dto.SendMessageDto;
import com.example.szakdoga.repository.FilesRepository;
import com.example.szakdoga.repository.SendMessageRepository;
import com.example.szakdoga.repository.UserRepository;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SendMessageService {

    @Autowired
    private SendMessageRepository sendMessageRepository;
    public List<ReceiverAllDto> getAllMessagesForReceiver(Integer receiverId) {
        List<SendMessage> messages = sendMessageRepository.findAllLatestIndividualMessagesByReceiverUserId(receiverId);
        List<ReceiverAllDto> allDetails = new ArrayList<>();

        for (SendMessage message : messages) {
            ReceiverAllDto receiverAllDto = new ReceiverAllDto();
            receiverAllDto.setId(message.getSenderUser().getId());
            receiverAllDto.setUsername(message.getSenderUser().getUsername());
            receiverAllDto.setMessage_content(message.getMessage_content());
            receiverAllDto.setTimestamp(message.getTimestamp());
            allDetails.add(receiverAllDto);
        }
        return allDetails;
    }

    public List<SendMessage> getConversation(Integer senderId, Integer receiverId) {
        return sendMessageRepository.findIndividualMessagesBetweenUsers(senderId, receiverId);
    }

    public List<GroupMessages> getGroupChatMessages(Integer userId1, Integer userId2) {
        List<Object[]> rawMessages = sendMessageRepository.findByUserIdsAndGroupChat(userId1, userId2);

        Map<String, List<GroupMessage>> groupedMessages = new HashMap<>();

        for (Object[] rawMessage : rawMessages) {
            System.out.println(rawMessage);
            Integer messageId = (Integer) rawMessage[0];
            String receiverUsername = (String) rawMessage[1];
            Integer receiverId = (Integer) rawMessage[2];
            Integer senderId = (Integer) rawMessage[3];
            String senderUsername = (String) rawMessage[4];
            String content = (String) rawMessage[5];
            // Az üzenet timestampje a szerver időpontja, nekünk formázni kell
            LocalDateTime timestamp = (LocalDateTime) rawMessage[6];
            String formattedTimestamp = formatTimestamp(timestamp);

            if (!groupedMessages.containsKey(receiverUsername)) {
                groupedMessages.put(receiverUsername, new ArrayList<>());
            }

            groupedMessages.get(receiverUsername).add(new GroupMessage(messageId, senderId, receiverId, senderUsername, content, formattedTimestamp));
        }

        return groupedMessages.entrySet()
                .stream()
                .map(entry -> new GroupMessages(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

    }

    private String formatTimestamp(LocalDateTime timestamp) {
        // LocalDateTime formázása DateTimeFormatterrel
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return timestamp.format(formatter);
    }

}
