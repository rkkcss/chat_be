package com.daniinc.chatapp.web.websocket;

import com.daniinc.chatapp.service.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long userId, MessageDTO messageDTO) {
        messagingTemplate.convertAndSendToUser(userId.toString(), "/queue/notifications", messageDTO);
    }
}
