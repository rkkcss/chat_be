package com.daniinc.chatapp.service;

import com.daniinc.chatapp.domain.Participant;
import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.repository.ParticipantRepository;
import com.daniinc.chatapp.service.dto.ParticipantDTO;
import com.daniinc.chatapp.service.dto.UserDTO;
import com.daniinc.chatapp.service.mapper.ParticipantMapper;
import com.daniinc.chatapp.service.mapper.UserMapper;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.daniinc.chatapp.domain.Participant}.
 */
@Service
@Transactional
public class ParticipantService {

    private final Logger log = LoggerFactory.getLogger(ParticipantService.class);

    private final ParticipantRepository participantRepository;

    private final ParticipantMapper participantMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public ParticipantService(
        ParticipantRepository participantRepository,
        ParticipantMapper participantMapper,
        UserService userService,
        UserMapper userMapper
    ) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Save a participant.
     *
     * @param participantDTO the entity to save.
     * @return the persisted entity.
     */
    public ParticipantDTO save(ParticipantDTO participantDTO) {
        log.debug("Request to save Participant : {}", participantDTO);
        Participant participant = participantMapper.toEntity(participantDTO);
        participant = participantRepository.save(participant);
        return participantMapper.toDto(participant);
    }

    /**
     * Update a participant.
     *
     * @param participantDTO the entity to save.
     * @return the persisted entity.
     */
    public ParticipantDTO update(ParticipantDTO participantDTO) {
        log.debug("Request to update Participant : {}", participantDTO);
        Participant participant = participantMapper.toEntity(participantDTO);
        participant = participantRepository.save(participant);
        return participantMapper.toDto(participant);
    }

    /**
     * Partially update a participant.
     *
     * @param participantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParticipantDTO> partialUpdate(ParticipantDTO participantDTO) {
        log.debug("Request to partially update Participant : {}", participantDTO);

        return participantRepository
            .findById(participantDTO.getId())
            .map(existingParticipant -> {
                participantMapper.partialUpdate(existingParticipant, participantDTO);

                return existingParticipant;
            })
            .map(participantRepository::save)
            .map(participantMapper::toDto);
    }

    /**
     * Get all the participants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParticipantDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Participants");
        return participantRepository.findAll(pageable).map(participantMapper::toDto);
    }

    /**
     * Get one participant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParticipantDTO> findOne(Long id) {
        log.debug("Request to get Participant : {}", id);
        return participantRepository.findById(id).map(participantMapper::toDto);
    }

    /**
     * Delete the participant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Participant : {}", id);
        participantRepository.deleteById(id);
    }

    public Set<UserDTO> filterListByLoggedInUser(Set<Participant> participants) {
        log.debug("Request to filter list by id");

        return participants
            .stream()
            .map(participant -> userMapper.userToUserDTO(participant.getUser())) // Assuming UserDTO constructor takes User entity
            .collect(Collectors.toSet());
    }
}
