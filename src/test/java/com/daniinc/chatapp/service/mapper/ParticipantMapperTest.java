package com.daniinc.chatapp.service.mapper;

import static com.daniinc.chatapp.domain.ParticipantAsserts.*;
import static com.daniinc.chatapp.domain.ParticipantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParticipantMapperTest {

    private ParticipantMapper participantMapper;

    @BeforeEach
    void setUp() {
        participantMapper = new ParticipantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getParticipantSample1();
        var actual = participantMapper.toEntity(participantMapper.toDto(expected));
        assertParticipantAllPropertiesEquals(expected, actual);
    }
}
