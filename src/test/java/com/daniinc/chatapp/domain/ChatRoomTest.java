package com.daniinc.chatapp.domain;

import static com.daniinc.chatapp.domain.ChatRoomTestSamples.*;
import static com.daniinc.chatapp.domain.MessageTestSamples.*;
import static com.daniinc.chatapp.domain.ParticipantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.daniinc.chatapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChatRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoom.class);
        ChatRoom chatRoom1 = getChatRoomSample1();
        ChatRoom chatRoom2 = new ChatRoom();
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);

        chatRoom2.setId(chatRoom1.getId());
        assertThat(chatRoom1).isEqualTo(chatRoom2);

        chatRoom2 = getChatRoomSample2();
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);
    }

    @Test
    void messageTest() {
        ChatRoom chatRoom = getChatRoomRandomSampleGenerator();
        Message messageBack = getMessageRandomSampleGenerator();

        chatRoom.addMessage(messageBack);
        assertThat(chatRoom.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getChatRoom()).isEqualTo(chatRoom);

        chatRoom.removeMessage(messageBack);
        assertThat(chatRoom.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getChatRoom()).isNull();

        chatRoom.messages(new HashSet<>(Set.of(messageBack)));
        assertThat(chatRoom.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getChatRoom()).isEqualTo(chatRoom);

        chatRoom.setMessages(new HashSet<>());
        assertThat(chatRoom.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getChatRoom()).isNull();
    }

    @Test
    void participantTest() {
        ChatRoom chatRoom = getChatRoomRandomSampleGenerator();
        Participant participantBack = getParticipantRandomSampleGenerator();

        chatRoom.addParticipant(participantBack);
        assertThat(chatRoom.getParticipants()).containsOnly(participantBack);
        assertThat(participantBack.getChatRoom()).isEqualTo(chatRoom);

        chatRoom.removeParticipant(participantBack);
        assertThat(chatRoom.getParticipants()).doesNotContain(participantBack);
        assertThat(participantBack.getChatRoom()).isNull();

        chatRoom.participants(new HashSet<>(Set.of(participantBack)));
        assertThat(chatRoom.getParticipants()).containsOnly(participantBack);
        assertThat(participantBack.getChatRoom()).isEqualTo(chatRoom);

        chatRoom.setParticipants(new HashSet<>());
        assertThat(chatRoom.getParticipants()).doesNotContain(participantBack);
        assertThat(participantBack.getChatRoom()).isNull();
    }
}
