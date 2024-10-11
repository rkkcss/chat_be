package com.daniinc.chatapp.repository;

import com.daniinc.chatapp.domain.AvatarImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AvatarImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvatarImageRepository extends JpaRepository<AvatarImage, Long> {}
