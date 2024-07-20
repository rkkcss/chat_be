package com.daniinc.chatapp.repository;

import com.daniinc.chatapp.domain.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Participant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query("select participant from Participant participant where participant.user.login = ?#{authentication.name}")
    List<Participant> findByUserIsCurrentUser();

    @Query("select p from Participant p where p.chatRoom.id = ?1 and p.user.id = ?2")
    Optional<Participant> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
