package com.bookface.postsservice.repository;

import com.bookface.postsservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostsRepository extends MongoRepository<Post, String>{
    List<Post> findByAuthorIdAndPublishedTrue(String authorId);
    List<Post> findByAuthorIdAndPublishedFalse(String authorId);
}
