package com.bookface.postsservice.repository;
import com.bookface.postsservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentsRepository extends MongoRepository <Comment,String> {

}
