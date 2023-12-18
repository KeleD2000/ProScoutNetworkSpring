package com.example.szakdoga.services;

import com.example.szakdoga.model.Player;
import com.example.szakdoga.model.Scout;
import com.example.szakdoga.model.SendMessage;
import com.example.szakdoga.model.dto.SendMessageDto;
import com.example.szakdoga.repository.SendMessageRepository;
import com.example.szakdoga.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendMessageService {
    private final SendMessageRepository sendMessageRepository;
    private final UserRepository userRepository;

    public SendMessageService(SendMessageRepository sendMessageRepository, UserRepository userRepository) {
        this.sendMessageRepository = sendMessageRepository;
        this.userRepository = userRepository;
    }

    public List<SendMessageDto> getAllMessage(Integer senderUserId, Integer receiverUserId) {
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
    }
}
