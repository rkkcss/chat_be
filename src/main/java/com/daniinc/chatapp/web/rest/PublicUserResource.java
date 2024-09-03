package com.daniinc.chatapp.web.rest;

import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.repository.UserRepository;
import com.daniinc.chatapp.service.UserService;
import com.daniinc.chatapp.service.dto.UserDTO;
import java.util.*;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class PublicUserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey")
    );

    private final Logger log = LoggerFactory.getLogger(PublicUserResource.class);

    private final UserService userService;
    private final UserRepository userRepository;

    public PublicUserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * {@code GET /users} : get all users with only public information - calling this method is allowed for anyone.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllPublicUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get all public User names");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<UserDTO> page = userService.getAllPublicUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    @GetMapping("/find-users")
    public ResponseEntity<List<UserDTO>> searchInUsers(@RequestParam String query, Pageable pageable) {
        Page<UserDTO> page = userRepository.findUsersBySearch(query, pageable).map(UserDTO::new);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/user/{login}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(login);
        UserDTO result = user.map(UserDTO::new).orElse(null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
