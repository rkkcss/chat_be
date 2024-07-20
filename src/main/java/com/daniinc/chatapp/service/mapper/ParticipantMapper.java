package com.daniinc.chatapp.service.mapper;

import com.daniinc.chatapp.domain.ChatRoom;
import com.daniinc.chatapp.domain.Participant;
import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.service.dto.ChatRoomDTO;
import com.daniinc.chatapp.service.dto.ParticipantDTO;
import com.daniinc.chatapp.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participant} and its DTO {@link ParticipantDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParticipantMapper extends EntityMapper<ParticipantDTO, Participant> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    //    @Mapping(target = "chatRoom", source = "chatRoom", qualifiedByName = "chatRoomId")
    ParticipantDTO toDto(Participant s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("chatRoomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatRoomDTO toDtoChatRoomId(ChatRoom chatRoom);
}
