package com.kaushal.Create_Post_Service.service;

import com.kaushal.Create_Post_Service.entity.Post;
import com.kaushal.Create_Post_Service.repository.PostRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private final WebClient webClient;

    public PostService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/").build();
    }

    private boolean isTokenValid(String token, String email) {
        return Boolean.TRUE.equals(webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/gateway/check-token-validity")
                        .queryParam("email", email)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Boolean>() {})
                .doOnNext(valid -> System.out.println("WebClient Response: " + valid)) // ✅ Print inside reactive pipeline
                .doOnError(error -> System.out.println("WebClient Error: " + error.getMessage())) // ✅ Print errors
                .block());
    }

    public List<Post> getAllPosts(String token, String email) {
        System.out.println("createPost() method called in PostService...");
        if (email.isEmpty()) {
            System.out.println("Email is empty, returning false.");
            return null;
        }

        boolean isValid = this.isTokenValid(token, email);
        System.out.println("Token validity check result: " + isValid);
        if (!isValid) {
            System.out.println("Token is invalid, returning false.");
            return null;
        }
        System.out.println("All Checks Done!");
        return postRepository.findAll();
    }

    public Boolean createPost(Post newPost, String email, String token) {
        System.out.println("createPost() method called in PostService...");

        if (email.isEmpty()) {
            System.out.println("Email is empty, returning false.");
            return false;
        }

        boolean isValid = this.isTokenValid(token, email);

        System.out.println("Token validity check result: " + isValid);

        if (!isValid) {
            System.out.println("Token is invalid, returning false.");
            return false;
        }

        System.out.println("All Checks Done!");

        if (newPost == null) {
            System.out.println("New Post is null, returning false.");
            return false;
        }

        postRepository.save(newPost);
        System.out.println("Post successfully saved to DB!");
        return true;
    }

    public boolean updatePost(Post updatedPost, String email, String token, String postID) {
        System.out.println("updatePost() method called in PostService...");

        if (email.isEmpty()) {
            System.out.println("Email is empty, returning false.");
            return false;
        }

        boolean isValid = this.isTokenValid(token, email);
        System.out.println("Token validity check result: " + isValid);
        if (!isValid) {
            System.out.println("Token is invalid, returning false.");
            return false;
        }
        System.out.println("All Checks Done!");


        if (postID == null) {
            return false;
        }
        ObjectId id = new ObjectId(postID);
        Optional<Post> foundPost = postRepository.findById(id);
        if (foundPost.isPresent()) {
            if (updatedPost.getCaption() == null) {
                System.out.println("Updated Post is null, returning false.");
                return false;
            }
            Post currentPost = foundPost.get();
            currentPost.setCaption(updatedPost.getCaption());
            postRepository.save(currentPost);
            System.out.println("Post updated successfully!");
            return true;
        }
        return false;
    }

    public HashMap<String, String> getPostById(String token, String email, String postID) {
        System.out.println("getPostById() method called in PostService...");
        if (email.isEmpty()) {
            System.out.println("Email is empty, returning false.");
            return null;
        }

        boolean isValid = this.isTokenValid(token, email);
        System.out.println("Token validity check result: " + isValid);
        if (!isValid) {
            System.out.println("Token is invalid, returning false.");
            return null;
        }
        System.out.println("All Checks Done!");

        if (postID == null) {
            return null;
        }

        ObjectId id = new ObjectId(postID);
        Optional<Post> foundPost = postRepository.findById(id);
        if (foundPost.isPresent()) {
            Post post = foundPost.get();
            HashMap<String, String> postInfo = new HashMap<>();
            postInfo.put("ObjectID", post.getId().toString());
            postInfo.put("imageURL", post.getImageURL());
            postInfo.put("userImageURL", post.getUserImageURL());
            postInfo.put("userEmail", post.getUserEmail());
            postInfo.put("username", post.getUsername());
            postInfo.put("totalLikes", post.getTotalLikes());
            postInfo.put("totalComments", post.getTotalComments());
            postInfo.put("caption", post.getCaption());
            return postInfo;
        }
        return null;
    }

    public boolean deletePost(String token, String email, String postID) {
        System.out.println("deletePost() method called in PostService...");
        if (email.isEmpty()) {
            System.out.println("Email is empty, returning false.");
            return false;
        }

        boolean isValid = this.isTokenValid(token, email);
        System.out.println("Token validity check result: " + isValid);
        if (!isValid) {
            System.out.println("Token is invalid, returning false.");
            return false;
        }
        System.out.println("All Checks Done!");

        if (postID == null) {
            return false;
        }

        ObjectId id = new ObjectId(postID);
        Optional<Post> foundPost = postRepository.findById(id);
        if (foundPost.isPresent()) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
