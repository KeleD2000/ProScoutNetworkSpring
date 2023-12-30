package com.example.szakdoga.services;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.model.SendMessage;
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

    /*public List<SendMessageDto> getAllMessage(Integer senderUserId, Integer receiverUserId) {
        List<SendMessage> messages = sendMessageRepository.findAllBySenderUserIdAndReceiverUserId(senderUserId,receiverUserId);
        List<SendMessage> messages1 = sendMessageRepository.findAllBySenderUserIdAndReceiverUserId(receiverUserId, senderUserId);
        messages.addAll(messages1);
        messages.sort(Comparator.comparing(SendMessage::getMessage_id));
        List<SendMessageDto> messageDtoList = messages.stream()
                .sorted(Comparator.comparing(SendMessage::getMessage_id))
                .map(m -> {
                    SendMessageDto messageDto = new SendMessageDto();
                    messageDto.setId(m.getMessage_id());
                    messageDto.setMessage_content(m.getMessage_content());
                    messageDto.setReaded(m.getReaded());
                    messageDto.setDateTime(m.getDateTime());
                    messageDto.setSenderUserId(m.getSenderUser().getId());
                    messageDto.setReceiverUserId(m.getReceiverUser().getId());
                    messageDto.setSenderUserProfilePicture(m.getSenderUser().getProfilePictureName());
                    messageDto.setReceiverUserProfilePicture(m.getReceiverUser().getProfilePictureName());

                    Player senderPlayer = m.getSenderUser().getPlayer();
                    if (senderPlayer != null) {
                        messageDto.setSenderUserFirstName(senderPlayer.getFirst_name());
                        messageDto.setSenderUserLastName(senderPlayer.getLast_name());
                    }

                    Scout senderScout = m.getSenderUser().getScout();
                    if (senderScout != null) {
                        messageDto.setSenderUserFirstName(senderScout.getFirst_name());
                        messageDto.setSenderUserLastName(senderScout.getLast_name());
                    }

                    Player receiverPlayer = m.getReceiverUser().getPlayer();
                    if (receiverPlayer != null) {
                        messageDto.setReceiverUserFirstName(receiverPlayer.getFirst_name());
                        messageDto.setReceiverUserLastName(receiverPlayer.getLast_name());
                    }

                    Scout receiverScout = m.getReceiverUser().getScout();
                    if (receiverScout != null) {
                        messageDto.setReceiverUserFirstName(receiverScout.getFirst_name());
                        messageDto.setReceiverUserLastName(receiverScout.getLast_name());
                    }

                    return messageDto;
                })
                .collect(Collectors.toList());

        return messageDtoList;
    }*/
}
