package com.kaushal.Create_Post_Service.repository;

import com.kaushal.Create_Post_Service.entity.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, ObjectId> {
}
