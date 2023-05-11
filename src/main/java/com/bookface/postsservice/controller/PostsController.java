package com.bookface.postsservice.controller;

import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.dto.PostsResponse;
import com.bookface.postsservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<String> createPost(@ModelAttribute PostsRequest postRequest) throws Exception {
        try {
            postsService.createPost(postRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostsResponse> getAllPosts() {
        return postsService.getAllPosts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getPostById(@PathVariable String id) {
        try {
            PostsResponse postsResponse = postsService.getPostById(id);
            return ResponseEntity.status(HttpStatus.OK).body(postsResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such post exists.");
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updatePost(@PathVariable String id, @RequestBody PostUpdateRequest updatePostBody) {
        postsService.updatePost(id, updatePostBody.title, updatePostBody.body);
        return ResponseEntity.status(HttpStatus.OK).body("Post Updated Successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deletePost(@PathVariable String id) {
        postsService.deletePost(id);
        //TODO: Delete associated image from firebase.
        return ResponseEntity.status(HttpStatus.OK).body("Post Deleted Successfully");
    }

    @PostMapping("/clap/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void clap(@PathVariable String id) {
        postsService.clap(id);
    }

    @PostMapping("/commentInc/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void commentInc(@PathVariable String id) {
        postsService.incrementComments(id);
    }

    @PostMapping("/commentDec/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void commentDec(@PathVariable String id) {
        postsService.decrementComments(id);
    }

    static public class PostUpdateRequest {
        public String title;
        public String body;
    }



}
