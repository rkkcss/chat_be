package com.daniinc.chatapp.repository;

import com.daniinc.chatapp.domain.Message;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select message from Message message where message.user.login = ?#{authentication.name}")
    List<Message> findByUserIsCurrentUser();

    @Query(
        "SELECT m FROM Message m WHERE m.chatRoom.id = ?1 AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chatRoom.id = ?1)"
    )
    Optional<Message> findLastMessageByRoomId(Long roomId);

    @Query("select m from Message m where m.chatRoom.id = ?1 order by m.createdAt desc")
    Page<Message> findByRoomId(Long roomId, Pageable pageable);
}
