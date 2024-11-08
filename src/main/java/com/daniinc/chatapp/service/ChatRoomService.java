package com.daniinc.chatapp.service;

import com.daniinc.chatapp.domain.ChatRoom;
import com.daniinc.chatapp.domain.Message;
import com.daniinc.chatapp.domain.Participant;
import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.repository.ChatRoomRepository;
import com.daniinc.chatapp.repository.MessageRepository;
import com.daniinc.chatapp.repository.ParticipantRepository;
import com.daniinc.chatapp.repository.UserRepository;
import com.daniinc.chatapp.service.dto.AdminUserDTO;
import com.daniinc.chatapp.service.dto.ChatRoomDTO;
import com.daniinc.chatapp.service.dto.MessageDTO;
import com.daniinc.chatapp.service.dto.UserDTO;
import com.daniinc.chatapp.service.mapper.ChatRoomMapper;
import com.daniinc.chatapp.service.mapper.MessageMapper;
import com.daniinc.chatapp.service.mapper.UserMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ChatRoomService(
        ChatRoomRepository chatRoomRepository,
        ChatRoomMapper chatRoomMapper,
        UserService userService,
        MessageRepository messageRepository,
        MessageMapper messageMapper,
        ParticipantRepository participantRepository,
        UserRepository userRepository,
        UserMapper userMapper
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMapper = chatRoomMapper;
        this.userService = userService;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

        User foundUser = user.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return chatRoomRepository.findChatRoomsByUserId(foundUser.getId(), pageable).map(chatRoomMapper::toDto);
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
        Optional<AdminUserDTO> loggedInUser = userService.getUserWithAuthorities().map(userMapper::userToAdminUserDTO);
        Optional<ChatRoomDTO> resultRoom = chatRoomRepository.findById(id).map(chatRoomMapper::toDto);

        AdminUserDTO userDto = loggedInUser.orElseThrow(() -> new RuntimeException("User not found!"));
        ChatRoomDTO chatRoomDto = resultRoom.orElseThrow(() -> new RuntimeException("Room not found!"));

        //      if we find the user in the participants wont throw error and return the chatroomDto
        chatRoomDto
            .getParticipants()
            .stream()
            .filter(e -> e.getLogin().equals(userDto.getLogin()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Participant not found"));
        return resultRoom;
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

    public ResponseEntity<?> create(List<Long> userIds) {
        Optional<User> ownUser = userService.getUserWithAuthorities();

        if (ownUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = ownUser.orElseThrow();
        userIds.add(user.getId());

        Optional<ChatRoom> existsRoom = chatRoomRepository.findRoomsByUserIds(userIds, userIds.size());

        return existsRoom
            .map(room -> new ResponseEntity<>(chatRoomMapper.toDto(room), HttpStatus.FOUND))
            .orElseGet(() -> {
                ChatRoom chatRoom = new ChatRoom();
                ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

                Set<Participant> participantList = new HashSet<>();
                userIds.forEach(id -> {
                    Optional<User> userOptional = userRepository.findById(id);
                    userOptional.ifPresent(foundUser -> {
                        Participant participant = new Participant();
                        participant.setUser(foundUser);
                        participant.setChatRoom(savedChatRoom);
                        participantList.add(participant);
                        participantRepository.save(participant);
                    });
                });

                savedChatRoom.setParticipants(participantList);
                ChatRoomDTO result = chatRoomMapper.toDto(savedChatRoom);
                return ResponseEntity.status(HttpStatus.CREATED).body(result);
            });
    }
}
