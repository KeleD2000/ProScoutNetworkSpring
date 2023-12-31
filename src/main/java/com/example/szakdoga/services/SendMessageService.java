package com.example.szakdoga.services;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.model.SendMessage;
import com.example.szakdoga.model.User;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendMessageService {

    @Autowired
    private SendMessageRepository sendMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private FilesService filesService;

    public List<ReceiverAllDto> getAllMessagesForReceiver(Integer receiverId) {
        List<SendMessage> messages = sendMessageRepository.findAllLatestMessagesByReceiverUserId(receiverId);
        List<ReceiverAllDto> allDetails = new ArrayList<>();

        for (SendMessage message : messages) {
            ReceiverAllDto receiverAllDto = new ReceiverAllDto();
            receiverAllDto.setId(message.getSenderUser().getId());
            receiverAllDto.setUsername(message.getSenderUser().getUsername());
            receiverAllDto.setMessage_content(message.getMessage_content());
            allDetails.add(receiverAllDto);
        }
        return allDetails;
    }

    public List<SendMessage> getConversation(Integer senderId, Integer receiverId) {
        return sendMessageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }
}
