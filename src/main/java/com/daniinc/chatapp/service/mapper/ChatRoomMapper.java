package com.daniinc.chatapp.service.mapper;

import com.daniinc.chatapp.domain.ChatRoom;
import com.daniinc.chatapp.service.ChatRoomService;
import com.daniinc.chatapp.service.MessageService;
import com.daniinc.chatapp.service.ParticipantService;
import com.daniinc.chatapp.service.dto.ChatRoomDTO;
import com.daniinc.chatapp.service.dto.ParticipantDTO;
import java.util.Set;
import org.checkerframework.checker.units.qual.C;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * Mapper for the entity {@link ChatRoom} and its DTO {@link ChatRoomDTO}.
 */
@Mapper(componentModel = "spring")
public abstract class ChatRoomMapper implements EntityMapper<ChatRoomDTO, ChatRoom> {

    @Autowired
    @Lazy
    MessageService messageService;

    @Autowired
    @Lazy
    ParticipantService participantService;

    @Mapping(target = "lastMessage", expression = "java(messageService.findLastMessageByRoomId(chatRoom.getId()))")
    @Mapping(target = "participants", expression = "java(participantService.filterListByLoggedInUser(chatRoom.getParticipants()))")
    public abstract ChatRoomDTO toDto(ChatRoom chatRoom);
}
