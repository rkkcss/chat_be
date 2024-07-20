package com.daniinc.chatapp.domain;

import static com.daniinc.chatapp.domain.ChatRoomTestSamples.*;
import static com.daniinc.chatapp.domain.ParticipantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.daniinc.chatapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParticipantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Participant.class);
        Participant participant1 = getParticipantSample1();
        Participant participant2 = new Participant();
        assertThat(participant1).isNotEqualTo(participant2);

        participant2.setId(participant1.getId());
        assertThat(participant1).isEqualTo(participant2);

        participant2 = getParticipantSample2();
        assertThat(participant1).isNotEqualTo(participant2);
    }

    @Test
    void chatRoomTest() {
        Participant participant = getParticipantRandomSampleGenerator();
        ChatRoom chatRoomBack = getChatRoomRandomSampleGenerator();

        participant.setChatRoom(chatRoomBack);
        assertThat(participant.getChatRoom()).isEqualTo(chatRoomBack);

        participant.chatRoom(null);
        assertThat(participant.getChatRoom()).isNull();
    }
}
