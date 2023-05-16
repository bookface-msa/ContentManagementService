package com.bookface.postsservice.repository;

import com.bookface.postsservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostsRepository extends MongoRepository<Post, String>{
    //void editPost(String id, String postContent, String updatePart);
}
