package com.bookface.postsservice.repository;
import com.bookface.postsservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends MongoRepository <Comment,String> {
}



