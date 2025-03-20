package com.kaushal.Create_Post_Service.controller;

import com.kaushal.Create_Post_Service.entity.Post;
import com.kaushal.Create_Post_Service.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/get-all-posts")
    public ResponseEntity<List<Post>> findAllPosts(@RequestHeader ("Authorization") String authHeader,
                                                   @RequestParam String email) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.replace("Bearer ", "").trim();
        System.out.println("Critical checks begin...");
        List<Post> listOfPosts = postService.getAllPosts(token, email);
        if (!listOfPosts.isEmpty()) {
            return new ResponseEntity<>(listOfPosts, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/find-post-by-id")
    public ResponseEntity<HashMap<String, String>> findPostById(@RequestHeader ("Authorization") String authHeader,
                                                                @RequestParam String email,
                                                                @RequestHeader("postID") String postID) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.replace("Bearer ", "").trim();
        System.out.println("Critical checks begin...");

        HashMap<String, String> foundPost = postService.getPostById(token, email, postID);
        if (foundPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundPost, HttpStatus.FOUND);
    }

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

    @PostMapping("/update-post")
    public ResponseEntity<String> updatePost(@RequestHeader("Authorization") String authHeader,
                                             @RequestHeader("postID") String postID,
                                             @RequestParam String email, @RequestBody Post updatedPost) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.replace("Bearer ", "").trim();
        System.out.println("Critical check for post update begin...");
        boolean response = postService.updatePost(updatedPost, email, token, postID);
        if (response) {
            return new ResponseEntity<>("Post Updated!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to update post.", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete-post")
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String authHeader,
                                              @RequestParam String email,
                                              @RequestHeader("postID") String postID) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String token = authHeader.replace("Bearer ", "").trim();
        System.out.println("Critical check for post update begin...");
        boolean response = postService.deletePost(token, email, postID);
        if (response) {
            return new ResponseEntity<>("Post Deleted!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete post.", HttpStatus.BAD_REQUEST);
    }
}
