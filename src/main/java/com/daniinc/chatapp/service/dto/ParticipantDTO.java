package com.daniinc.chatapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.daniinc.chatapp.domain.Participant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ParticipantDTO implements Serializable {

    private Long id;

    private UserDTO user;

    //    private ChatRoomDTO chatRoom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    //    public ChatRoomDTO getChatRoom() {
    //        return chatRoom;
    //    }
    //
    //    public void setChatRoom(ChatRoomDTO chatRoom) {
    //        this.chatRoom = chatRoom;
    //    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipantDTO)) {
            return false;
        }

        ParticipantDTO participantDTO = (ParticipantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, participantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipantDTO{" +
            "id=" + getId() +
            ", user=" + getUser() +
            "}";
    }
}
