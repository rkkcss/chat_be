package com.daniinc.chatapp.web.websocket;

import com.daniinc.chatapp.service.UserService;
import com.daniinc.chatapp.service.dto.AdminUserDTO;
import com.daniinc.chatapp.service.dto.UserDTO;
import java.security.Principal;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class ActiveUserService {

    private static final Logger log = LoggerFactory.getLogger(ActiveUserService.class);

    private final UserService userService;
    private final ActiveUsers activeUsers;
    private final SimpMessageSendingOperations messagingTemplate;

    public ActiveUserService(UserService userService, ActiveUsers activeUsers, SimpMessageSendingOperations messagingTemplate) {
        this.userService = userService;
        this.activeUsers = activeUsers;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = Objects.requireNonNull(event.getUser()).getName();
        Optional<AdminUserDTO> adminUserDTO = userService.getUserWithAuthoritiesByLogin(username).map(AdminUserDTO::new);
        adminUserDTO.ifPresent(activeUsers::addUser);
        //        messagingTemplate.convertAndSend("/ws/receive/users", activeUsers.getUsers());
    }

    @MessageMapping("/ws/getusers")
    @SendTo("/topic/users")
    public Collection<AdminUserDTO> handleActiveUsers(Principal principal) {
        return activeUsers.getUsers();
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        log.info("Web socket connection closed: {}", sessionId); // Session ID logolása
        String username = Objects.requireNonNull(event.getUser()).getName();

        Optional<AdminUserDTO> adminUserDTO = userService.getUserWithAuthoritiesByLogin(username).map(AdminUserDTO::new);
        adminUserDTO.ifPresent(activeUsers::removeUser);
        messagingTemplate.convertAndSend("/topic/users", activeUsers.getUsers());
    }
}
