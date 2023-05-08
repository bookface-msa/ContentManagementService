package com.bookface.postsservice.controller;

import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.dto.PostsResponse;
import com.bookface.postsservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostsRequest postRequest) throws Exception {
        try {
            postsService.createPost(postRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");
        }catch(Exception e){
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
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such post exists.");
        }
    }


}
