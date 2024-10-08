package com.daniinc.chatapp.web.rest;

import com.daniinc.chatapp.repository.MessageRepository;
import com.daniinc.chatapp.service.MessageService;
import com.daniinc.chatapp.service.dto.MessageDTO;
import com.daniinc.chatapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.daniinc.chatapp.domain.Message}.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MessageService messageService;

    private final MessageRepository messageRepository;

    public MessageResource(MessageService messageService, MessageRepository messageRepository) {
        this.messageService = messageService;
        this.messageRepository = messageRepository;
    }

    /**
     * {@code POST  /messages} : Create a new message.
     *
     * @param messageDTO the messageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new messageDTO, or with status {@code 400 (Bad Request)} if the message has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to save Message : {}", messageDTO);
        if (messageDTO.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (messageDTO.getText().isEmpty()) {
            throw new BadRequestAlertException("Empty field", ENTITY_NAME, "text");
        }
        messageDTO = messageService.save(messageDTO);
        return ResponseEntity.created(new URI("/api/messages/" + messageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, messageDTO.getId().toString()))
            .body(messageDTO);
    }

    /**
     * {@code PUT  /messages/:id} : Updates an existing message.
     *
     * @param id the id of the messageDTO to save.
     * @param messageDTO the messageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageDTO,
     * or with status {@code 400 (Bad Request)} if the messageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the messageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> updateMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MessageDTO messageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Message : {}, {}", id, messageDTO);
        if (messageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        messageDTO = messageService.update(messageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageDTO.getId().toString()))
            .body(messageDTO);
    }

    /**
     * {@code PATCH  /messages/:id} : Partial updates given fields of an existing message, field will ignore if it is null
     *
     * @param id the id of the messageDTO to save.
     * @param messageDTO the messageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated messageDTO,
     * or with status {@code 400 (Bad Request)} if the messageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the messageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the messageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MessageDTO> partialUpdateMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MessageDTO messageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Message partially : {}, {}", id, messageDTO);
        if (messageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, messageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!messageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MessageDTO> result = messageService.partialUpdate(messageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, messageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /messages} : get all the messages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of messages in body.
     */
    @GetMapping("")
    public ResponseEntity<Page<MessageDTO>> getAllMessages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Messages");
        Page<MessageDTO> page = messageService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    /**
     * {@code GET  /messages/:id} : get the "id" message.
     *
     * @param id the id of the messageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the messageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<Page<MessageDTO>> getMessage(@PathVariable("roomId") Long roomId, Pageable pageable) {
        log.debug("REST request to get Message : {}", roomId);
        Page<MessageDTO> page = messageService.getMessegesByRoomId(roomId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    /**
     * {@code DELETE  /messages/:id} : delete the "id" message.
     *
     * @param id the id of the messageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/get-all-media/{roomId}")
    public ResponseEntity<List<String>> getAllMedia(@PathVariable Long roomId) {
        log.debug("REST request to get all media");
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getRoomMediaFiles(roomId));
    }
}
