package com.daniinc.chatapp.service;

import com.daniinc.chatapp.domain.ChatRoom;
import com.daniinc.chatapp.domain.Message;
import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.repository.ChatRoomRepository;
import com.daniinc.chatapp.repository.MessageRepository;
import com.daniinc.chatapp.service.dto.ChatRoomDTO;
import com.daniinc.chatapp.service.dto.MessageDTO;
import com.daniinc.chatapp.service.mapper.ChatRoomMapper;
import com.daniinc.chatapp.service.mapper.MessageMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.daniinc.chatapp.domain.ChatRoom}.
 */
@Service
@Transactional
public class ChatRoomService {

    private final Logger log = LoggerFactory.getLogger(ChatRoomService.class);

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMapper chatRoomMapper;
    private final UserService userService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public ChatRoomService(
        ChatRoomRepository chatRoomRepository,
        ChatRoomMapper chatRoomMapper,
        UserService userService,
        MessageRepository messageRepository,
        MessageMapper messageMapper
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMapper = chatRoomMapper;
        this.userService = userService;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    /**
     * Save a chatRoom.
     *
     * @param chatRoomDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatRoomDTO save(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to save ChatRoom : {}", chatRoomDTO);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomDTO);
        chatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toDto(chatRoom);
    }

    /**
     * Update a chatRoom.
     *
     * @param chatRoomDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatRoomDTO update(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to update ChatRoom : {}", chatRoomDTO);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomDTO);
        chatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toDto(chatRoom);
    }

    /**
     * Partially update a chatRoom.
     *
     * @param chatRoomDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatRoomDTO> partialUpdate(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to partially update ChatRoom : {}", chatRoomDTO);

        return chatRoomRepository
            .findById(chatRoomDTO.getId())
            .map(existingChatRoom -> {
                chatRoomMapper.partialUpdate(existingChatRoom, chatRoomDTO);

                return existingChatRoom;
            })
            .map(chatRoomRepository::save)
            .map(chatRoomMapper::toDto);
    }

    /**
     * Get all the chatRooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatRoomDTO> findAll(Pageable pageable) {
        Optional<User> user = userService.getUserWithAuthorities();
        log.debug("Request to get all ChatRooms");
        //        Page<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserId(user.get().getId(), pageable);
        return chatRoomRepository.findChatRoomsByUserId(user.get().getId(), pageable).map(chatRoomMapper::toDto);
    }

    /**
     * Get one chatRoom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatRoomDTO> findOne(Long id) {
        log.debug("Request to get ChatRoom : {}", id);
        return chatRoomRepository.findById(id).map(chatRoomMapper::toDto);
    }

    /**
     * Delete the chatRoom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChatRoom : {}", id);
        chatRoomRepository.deleteById(id);
    }

    public MessageDTO saveMessage(Long roomId, MessageDTO messageDTO) {
        // Save message to the database
        Message message = messageMapper.toEntity(messageDTO);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        message.setChatRoom(chatRoom);
        Message savedMessage = messageRepository.save(message);
        return messageMapper.toDto(savedMessage);
    }
}