package com.bookface.postsservice.service;

import com.bookface.postsservice.dto.CommentRequest;
import com.bookface.postsservice.model.Comment;
import com.bookface.postsservice.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsService {
    private final CommentsRepository commentsRepository;



    public void createComment(CommentRequest commentRequest){
        Comment comment=Comment.builder()
                .post_id(commentRequest.getPost_id())
                .author(commentRequest.getAuthor())
                .content(commentRequest.getContent())
                .numb_likes(commentRequest.getNumb_likes())
                .createdAt(commentRequest.getCreatedAt()).build();
        commentsRepository.save(comment);
        log.info("comment{}is saved",comment.getId());

    }
}
