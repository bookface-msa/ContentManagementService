package com.bookface.postsservice.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.bookface.postsservice.dto.CategoriesRequest;
import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.dto.PostsResponse;
import com.bookface.postsservice.exceptions.BadRequestException;
import com.bookface.postsservice.service.PostsService;
import com.bookface.postsservice.controller.CategoriesController;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.SysexMessage;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/content/api/post")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsService postsService;

    private final CategoriesController categoriesController;

    @PostMapping
    public ResponseEntity<String> createPost(@ModelAttribute PostsRequest postRequest, HttpServletRequest request ) throws Exception {
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
            postsService.createPost(postRequest, request);
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
    @GetMapping("/published")
    public  List<PostsResponse> getPublishedPostsByAuthorId(HttpServletRequest request) {
        try {
            return postsService.getPublishedPostsByAuthorId(request);
        }catch (Exception e){
            log.info("Not Authenticated");
            return null;
        }
    }
    @GetMapping("/drafted")
    public  List<PostsResponse> getDraftedPostsByAuthorId(HttpServletRequest request) {
        try {
            return postsService.getDraftedPostsByAuthorId(request);
        }catch(Exception e){
            log.info("Not Authenticated");
            return null;
        }
    }

    @PostMapping("/publish/{id}")
    public  ResponseEntity publishPost(HttpServletRequest request, @PathVariable String id) {
        try {
            postsService.publishPost(request, id);
            return ResponseEntity.status(HttpStatus.OK).body("Post Published");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity updatePost(@PathVariable String id, @RequestBody PostUpdateRequest updatePostBody, HttpServletRequest request) {
        try {
            postsService.updatePost(id, updatePostBody.title, updatePostBody.body,request);
            return ResponseEntity.status(HttpStatus.OK).body("Post Updated Successfully");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deletePost(@PathVariable String id, HttpServletRequest request) {
        try {
            postsService.deletePost(id,request);
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
