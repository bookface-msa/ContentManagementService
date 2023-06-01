package com.bookface.postsservice.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.bookface.postsservice.dto.CategoriesRequest;
import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.dto.PostsResponse;
import com.bookface.postsservice.exceptions.BadRequestException;
import com.bookface.postsservice.service.PostsService;
import com.bookface.postsservice.controller.CategoriesController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.SysexMessage;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsService postsService;

    private final CategoriesController categoriesController;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostsRequest postRequest) throws Exception {

        System.out.println(postRequest);
        System.out.println("post requestttttt");
        List<String> categories = postRequest.getCategoryNames();
        try {
            categoriesController.createCategory(categories);
        }
        catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such post exists.");
        }
    }
    @GetMapping("/published/{authorId}")
    public  List<PostsResponse> getPublishedPostsByAuthorId(@PathVariable String authorId) {
        return postsService.getPublishedPostsByAuthorId(authorId);
    }
    @GetMapping("/drafted/{authorId}")
    public  List<PostsResponse> getDraftedPostsByAuthorId(@PathVariable String authorId) {
        return postsService.getDraftedPostsByAuthorId(authorId);
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
        try {
            postsService.deletePost(id);
            return ResponseEntity.status(HttpStatus.OK).body("Post Deleted Successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Post doesn't exist or failed to delete post");
        }
    }

    @PostMapping("/clap/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void clap(@PathVariable String id) {
        postsService.clap(id);
    }

    static public class PostUpdateRequest {
        public String title;
        public String body;
    }



}
