package com.daniinc.chatapp.service;

import com.daniinc.chatapp.domain.AvatarImage;
import com.daniinc.chatapp.domain.User;
import com.daniinc.chatapp.repository.AvatarImageRepository;
import com.daniinc.chatapp.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AvatarImageService {

    private final AvatarImageRepository avatarImageRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public AvatarImageService(AvatarImageRepository avatarImageRepository, UserRepository userRepository, UserService userService) {
        this.avatarImageRepository = avatarImageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public ResponseEntity<AvatarImage> createNewAvatar(String avatarImage) {
        //Optional<User> user = userService.getUserWithAuthorities();

        AvatarImage newAavatarImage = new AvatarImage();
        newAavatarImage.setUrl(avatarImage);

        AvatarImage result = avatarImageRepository.save(newAavatarImage);
        return ResponseEntity.ok().body(result);
    }

    public void deleteItems(List<AvatarImage> avatarImages) {
        avatarImageRepository.deleteAllById(avatarImages.stream().map(AvatarImage::getId).toList());
    }
}
