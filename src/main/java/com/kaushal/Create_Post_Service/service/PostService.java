package com.kaushal.Create_Post_Service.service;

import com.kaushal.Create_Post_Service.entity.Post;
import com.kaushal.Create_Post_Service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.sql.SQLOutput;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private final WebClient webClient;

    public PostService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/").build();
    }

    public Boolean createPost(Post newPost, String email, String token) {
        System.out.println("createPost() method called in PostService...");

        if (email.isEmpty()) {
            System.out.println("Email is empty, returning false.");
            return false;
        }

        boolean isValid = Boolean.TRUE.equals(webClient.get()
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


}
