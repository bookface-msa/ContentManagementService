package com.bookface.postsservice.service;

import com.bookface.postsservice.dto.CommentRequest;
import com.bookface.postsservice.dto.CommentResponse;
import com.bookface.postsservice.model.Comment;
import com.bookface.postsservice.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;


import java.util.List;

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

    public List<CommentResponse> getALLComments() {
      List<Comment> comments=commentsRepository.findAll();
      return comments.stream().map(this::mapToCommentResponse).toList();

    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .post_id(comment.getPost_id())
                .author(comment.getAuthor())
                .content(comment.getContent())
                .numb_likes(comment.getNumb_likes())
                .createdAt(comment.getCreatedAt())
                .build();

    }

    public CommentResponse getCommentById(String id) {
        Optional<Comment> comments=commentsRepository.findById(id);
        return comments.map(this::mapToCommentResponse).get();
    }
}
