package com.kaushal.Create_Post_Service.controller;

import com.kaushal.Create_Post_Service.entity.Post;
import com.kaushal.Create_Post_Service.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<String> createPost(@RequestHeader("Authorization") String authHeader,
                                             @RequestParam String email, @RequestBody Post newPost) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.replace("Bearer ", "").trim();
        System.out.println("Critical checks begin...");
        boolean response = postService.createPost(newPost, email, token);
        if (response) {
            return new ResponseEntity<>("Post Created!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to create post.", HttpStatus.BAD_REQUEST);
    }
}
