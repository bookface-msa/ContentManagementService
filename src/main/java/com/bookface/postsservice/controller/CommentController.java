package com.bookface.postsservice.controller;

import com.bookface.postsservice.dto.CommentRequest;
import com.bookface.postsservice.dto.CommentResponse;
import com.bookface.postsservice.model.Comment;
import com.bookface.postsservice.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentsService commentsService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createComment(@RequestBody CommentRequest commentRequest) throws Exception{
        try {
        commentsService.createComment(commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("comment created successfully.");}
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

}
@GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getAllComments(){
       return commentsService.getALLComments();
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
}
