package com.bookface.postsservice.controller;

import com.bookface.postsservice.dto.CommentRequest;
import com.bookface.postsservice.dto.CommentResponse;
import com.bookface.postsservice.service.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/comment/{postId}")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentsService commentsService;
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createComment(@RequestBody CommentRequest commentRequest,@PathVariable("postId") String postId) throws Exception{
        try {
            commentsService.createComment(commentRequest,postId);
            return ResponseEntity.status(HttpStatus.CREATED).body("comment created successfully.");}
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getAllComments(@PathVariable("postId") String postId){
        return commentsService.getALLComments(postId);

    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getCommentById(@PathVariable String id){
        try{
            CommentResponse commentResponse= commentsService.getCommentById(id);
            return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No comment with this ID exists.");
        }

    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateComment(@PathVariable String id, @RequestBody CommentRequest updateCommentContent) {
        commentsService.updateComment(id, updateCommentContent.getContent());
        return ResponseEntity.status(HttpStatus.OK).body("Comment Updated Successfully");
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteComment(@PathVariable String id, @PathVariable String postId) {
        commentsService.deleteComment(id,postId);
        return ResponseEntity.status(HttpStatus.OK).body("Comment Deleted Successfully");
    }
    @PostMapping("/LikesInc/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> commentInc(@PathVariable String id) {
        commentsService.incrementLikes(id);
        return ResponseEntity.status(HttpStatus.OK).body("Likes incremented");
    }

    @PostMapping("/LikesDec/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> commentDec(@PathVariable String id) {
        commentsService.decrementLikes(id);
        return ResponseEntity.status(HttpStatus.OK).body("Likes decremented");
    }


}
