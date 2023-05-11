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
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentsService commentsService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@RequestBody CommentRequest commentRequest){
        commentsService.createComment(commentRequest);

}
@GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getAllComments(){
    return null;
}
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updateComment(@PathVariable String id, @RequestBody CommentRequest updateCommentContent) {
        commentsService.updateComment(id, updateCommentContent.getContent());
        return ResponseEntity.status(HttpStatus.OK).body("Comment Updated Successfully");
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteComment(@PathVariable String id, @PathVariable String post_id) {
        commentsService.deleteComment(id,post_id);
        return ResponseEntity.status(HttpStatus.OK).body("Comment Deleted Successfully");
    }
    @PostMapping("/LikesInc/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void commentInc(@PathVariable String id) {
        commentsService.incrementLikes(id);
    }

    @PostMapping("/commentDec/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void commentDec(@PathVariable String id) {
        commentsService.decrementLikes(id);
    }


}
