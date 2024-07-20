package com.daniinc.chatapp.web.websocket;

import com.daniinc.chatapp.domain.ChatRoom;
import com.daniinc.chatapp.domain.Message;
import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.repository.ChatRoomRepository;
import com.daniinc.chatapp.service.ChatRoomService;
import com.daniinc.chatapp.service.UserService;
import com.daniinc.chatapp.service.dto.ChatRoomDTO;
import com.daniinc.chatapp.service.dto.MessageDTO;
import com.daniinc.chatapp.service.dto.UserDTO;
import com.daniinc.chatapp.service.mapper.ChatRoomMapper;
import com.daniinc.chatapp.service.mapper.UserMapper;
import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RoomService {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomMapper chatRoomMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat.sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public MessageDTO sendMessage(@DestinationVariable Long roomId, MessageDTO messageDTO, Principal principal) {
        //Search the logged in user
        String username = principal.getName();
        Optional<User> loggedInUser = userService.getUserWithAuthoritiesByLogin(username);
        if (loggedInUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        //Map to DTO
        UserDTO userDTO = userMapper.userToUserDTO(loggedInUser.get());
        //Find the chatroom
        Optional<ChatRoomDTO> chatRoomDTO = chatRoomRepository.findById(roomId).map(chatRoomMapper::toDto);

        messageDTO.setUser(userDTO);

        MessageDTO result = chatRoomService.saveMessage(roomId, messageDTO);
        //Send notification for all the users in the chatroom
        chatRoomDTO
            .get()
            .getParticipants()
            .forEach(user -> {
                notificationService.sendNotification(user.getId(), result);
            });

        return result;
    }

    @MessageMapping("/chat.newRoom")
    @SendTo("/topic/{userId}")
    public ChatRoomDTO createRoom(ChatRoom chatRoom) {
        return chatRoomService.save(chatRoomMapper.toDto(chatRoom));
    }
}
