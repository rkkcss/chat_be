package com.daniinc.chatapp.repository;

import com.daniinc.chatapp.domain.ChatRoom;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select cr from ChatRoom cr join cr.participants p where p.user.id = ?1")
    Page<ChatRoom> findChatRoomsByUserId(Long userId, Pageable pageable);
}
