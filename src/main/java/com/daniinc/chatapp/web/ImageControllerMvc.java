package com.daniinc.chatapp.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageControllerMvc {

    @Value("${photo.storage.location}")
    private String staticLocations;

    @GetMapping("/images/{date}/{imageName:.+}")
    public ResponseEntity<?> getPhoto(@PathVariable String date, @PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(staticLocations).resolve(date).resolve(imageName);
            Resource resource = new InputStreamResource(Files.newInputStream(imagePath));

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
