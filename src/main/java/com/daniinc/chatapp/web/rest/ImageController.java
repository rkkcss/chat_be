package com.daniinc.chatapp.web.rest;

import com.daniinc.chatapp.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String path = imageService.uploadImage(file);

            // Abszolút URL létrehozása a képhez
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String imageUrl = baseUrl + "/" + path;
            return new ResponseEntity<>(imageUrl, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
