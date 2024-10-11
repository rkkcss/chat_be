package com.daniinc.chatapp.web.rest;

import com.daniinc.chatapp.domain.AvatarImage;
import com.daniinc.chatapp.repository.AvatarImageRepository;
import com.daniinc.chatapp.service.AvatarImageService;
import com.daniinc.chatapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.daniinc.chatapp.domain.AvatarImage}.
 */
@RestController
@RequestMapping("/api/avatar-images")
@Transactional
public class AvatarImageResource {

    private final Logger log = LoggerFactory.getLogger(AvatarImageResource.class);

    private static final String ENTITY_NAME = "avatarImage";
    private final AvatarImageService avatarImageService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AvatarImageRepository avatarImageRepository;

    public AvatarImageResource(AvatarImageRepository avatarImageRepository, AvatarImageService avatarImageService) {
        this.avatarImageRepository = avatarImageRepository;
        this.avatarImageService = avatarImageService;
    }

    /**
     * {@code POST  /avatar-images} : Create a new avatarImage.
     *
     * @param avatarImage the avatarImage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new avatarImage, or with status {@code 400 (Bad Request)} if the avatarImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<AvatarImage> createAvatarImage(@RequestBody Map<String, String> avatarImage) throws URISyntaxException {
        String imageUrl = avatarImage.get("imageUrl");
        log.debug("REST request to save AvatarImage : {}", imageUrl);

        return avatarImageService.createNewAvatar(imageUrl);
    }

    /**
     * {@code PUT  /avatar-images/:id} : Updates an existing avatarImage.
     *
     * @param id the id of the avatarImage to save.
     * @param avatarImage the avatarImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avatarImage,
     * or with status {@code 400 (Bad Request)} if the avatarImage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the avatarImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AvatarImage> updateAvatarImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AvatarImage avatarImage
    ) throws URISyntaxException {
        log.debug("REST request to update AvatarImage : {}, {}", id, avatarImage);
        if (avatarImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avatarImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avatarImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        avatarImage = avatarImageRepository.save(avatarImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avatarImage.getId().toString()))
            .body(avatarImage);
    }

    /**
     * {@code PATCH  /avatar-images/:id} : Partial updates given fields of an existing avatarImage, field will ignore if it is null
     *
     * @param id the id of the avatarImage to save.
     * @param avatarImage the avatarImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated avatarImage,
     * or with status {@code 400 (Bad Request)} if the avatarImage is not valid,
     * or with status {@code 404 (Not Found)} if the avatarImage is not found,
     * or with status {@code 500 (Internal Server Error)} if the avatarImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AvatarImage> partialUpdateAvatarImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AvatarImage avatarImage
    ) throws URISyntaxException {
        log.debug("REST request to partial update AvatarImage partially : {}, {}", id, avatarImage);
        if (avatarImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, avatarImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!avatarImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AvatarImage> result = avatarImageRepository
            .findById(avatarImage.getId())
            .map(existingAvatarImage -> {
                if (avatarImage.getCreatedDate() != null) {
                    existingAvatarImage.setCreatedDate(avatarImage.getCreatedDate());
                }
                if (avatarImage.getUrl() != null) {
                    existingAvatarImage.setUrl(avatarImage.getUrl());
                }
                if (avatarImage.getName() != null) {
                    existingAvatarImage.setName(avatarImage.getName());
                }

                return existingAvatarImage;
            })
            .map(avatarImageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, avatarImage.getId().toString())
        );
    }

    /**
     * {@code GET  /avatar-images} : get all the avatarImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of avatarImages in body.
     */
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<AvatarImage> getAllAvatarImages() {
        log.debug("REST request to get all AvatarImages");
        return avatarImageRepository.findAll();
    }

    @GetMapping("public")
    public List<AvatarImage> getAllPublicAvatarImages() {
        log.debug("REST request to get all public AvatarImages");
        return avatarImageRepository.findAll();
    }

    /**
     * {@code GET  /avatar-images/:id} : get the "id" avatarImage.
     *
     * @param id the id of the avatarImage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the avatarImage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AvatarImage> getAvatarImage(@PathVariable("id") Long id) {
        log.debug("REST request to get AvatarImage : {}", id);
        Optional<AvatarImage> avatarImage = avatarImageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(avatarImage);
    }

    /**
     * {@code DELETE  /avatar-images/:id} : delete the "id" avatarImage.
     *
     * @param id the id of the avatarImage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvatarImage(@PathVariable("id") Long id) {
        log.debug("REST request to delete AvatarImage : {}", id);
        avatarImageRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAvatarImages(@RequestBody List<AvatarImage> avatarImages) {
        log.debug("REST request to delete AvatarImages : {}", avatarImages);
        avatarImageService.deleteItems(avatarImages);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, avatarImages.toString()))
            .build();
    }
}
