package com.daniinc.chatapp.web.websocket;

import com.daniinc.chatapp.service.dto.AdminUserDTO;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class ActiveUsers {

    private final Map<Long, AdminUserDTO> activeUsers = new ConcurrentHashMap<>();

    public void addUser(AdminUserDTO adminUserDTO) {
        if (adminUserDTO != null && adminUserDTO.getId() != null) {
            activeUsers.put(adminUserDTO.getId(), adminUserDTO);
        }
    }

    public void removeUser(AdminUserDTO adminUserDTO) {
        if (adminUserDTO != null && adminUserDTO.getId() != null) {
            activeUsers.remove(adminUserDTO.getId());
        }
    }

    public Collection<AdminUserDTO> getUsers() {
        return new ArrayList<>(activeUsers.values());
    }
}
