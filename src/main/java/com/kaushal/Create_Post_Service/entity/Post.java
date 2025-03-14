package com.kaushal.Create_Post_Service.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
@Data
public class Post {

    @Id
    private ObjectId id;
    private String imageURL;
    private String userImageURL;
    private String userEmail;
    private String username;
    private String totalLikes;
    private String totalComments;
    private String caption;
}
