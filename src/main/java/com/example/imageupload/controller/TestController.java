package com.example.imageupload.controller;

import com.example.imageupload.service.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private final FileStorageService fileStorageService;

    @GetMapping
    public Resource test(@RequestParam("file2") MultipartFile file, String label) {
        String message = "";
        String imageURL = "";

        try {
            imageURL = fileStorageService.save(file, label);
            System.out.println(imageURL + " image url");
            message = "Uploaded the image successfully: " + file.getOriginalFilename();
            System.out.println(message);

            System.out.println(fileStorageService.getAllImages());
        } catch (Exception e) {
            System.out.println("hh");
        }
        return null;
    }
}
