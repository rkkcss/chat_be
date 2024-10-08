package com.daniinc.chatapp.service;

import com.daniinc.chatapp.domain.Message;
import com.daniinc.chatapp.domain.Participant;
import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.repository.MessageRepository;
import com.daniinc.chatapp.repository.ParticipantRepository;
import com.daniinc.chatapp.service.dto.MessageDTO;
import com.daniinc.chatapp.service.dto.UserDTO;
import com.daniinc.chatapp.service.mapper.MessageMapper;
import com.daniinc.chatapp.service.mapper.UserMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.daniinc.chatapp.domain.Message}.
 */
@Service
@Transactional
public class MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;
    private final UserService userService;
    private final ParticipantRepository participantRepository;
    private final UserMapper userMapper;

    public MessageService(
        MessageRepository messageRepository,
        MessageMapper messageMapper,
        UserService userService,
        ParticipantRepository participantRepository,
        UserMapper userMapper
    ) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.userService = userService;
        this.participantRepository = participantRepository;
        this.userMapper = userMapper;
    }

    /**
     * Save a message.
     *
     * @param messageDTO the entity to save.
     * @return the persisted entity.
     */
    public MessageDTO save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        userService.getUserWithAuthorities().map(UserDTO::new).ifPresent(messageDTO::setUser);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    /**
     * Update a message.
     *
     * @param messageDTO the entity to save.
     * @return the persisted entity.
     */
    public MessageDTO update(MessageDTO messageDTO) {
        log.debug("Request to update Message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    /**
     * Partially update a message.
     *
     * @param messageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MessageDTO> partialUpdate(MessageDTO messageDTO) {
        log.debug("Request to partially update Message : {}", messageDTO);

        return messageRepository
            .findById(messageDTO.getId())
            .map(existingMessage -> {
                messageMapper.partialUpdate(existingMessage, messageDTO);

                return existingMessage;
            })
            .map(messageRepository::save)
            .map(messageMapper::toDto);
    }

    /**
     * Get all the messages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Messages");
        return messageRepository.findAll(pageable).map(messageMapper::toDto);
    }

    /**
     * Get one message by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MessageDTO> findOne(Long id) {
        log.debug("Request to get Message : {}", id);
        return messageRepository.findById(id).map(messageMapper::toDto);
    }

    /**
     * Delete the message by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Message : {}", id);
        messageRepository.deleteById(id);
    }

    public MessageDTO findLastMessageByRoomId(Long roomId) {
        return messageRepository.findLastMessageByRoomId(roomId).map(messageMapper::toDto).orElse(null);
    }

    public Page<MessageDTO> getMessegesByRoomId(Long roomId, Pageable pageable) {
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            Optional<Participant> participant = participantRepository.findByChatRoomIdAndUserId(roomId, user.get().getId());
            if (participant.isPresent()) {
                return messageRepository.findByRoomId(roomId, pageable).map(messageMapper::toDto);
            }
        }
        throw new UsernameNotFoundException("User not found");
    }

    public List<String> getRoomMediaFiles(Long roomId) {
        return messageRepository.findByMediaUrls(roomId).stream().map(Message::getMediaUrl).toList();
    }
}
