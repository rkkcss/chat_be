package com.daniinc.chatapp.repository;

import com.daniinc.chatapp.domain.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select cr from ChatRoom cr join cr.participants p where p.user.id = ?1")
    Page<ChatRoom> findChatRoomsByUserId(Long userId, Pageable pageable);

    @Query(
        "SELECT c FROM ChatRoom c WHERE (SELECT COUNT(p) FROM c.participants p WHERE p.user.id IN ?1) = ?2 AND SIZE(c.participants) = ?2"
    )
    Optional<ChatRoom> findRoomsByUserIds(@Param("userIds") List<Long> userIds, @Param("userCount") Integer userCount);
}
