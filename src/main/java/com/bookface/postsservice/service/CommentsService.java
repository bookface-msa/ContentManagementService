package com.bookface.postsservice.service;

import com.bookface.postsservice.dto.CommentRequest;
import com.bookface.postsservice.dto.CommentResponse;
import com.bookface.postsservice.model.Comment;
import com.bookface.postsservice.model.Post;
import com.bookface.postsservice.repository.CommentsRepository;
import com.bookface.postsservice.repository.PostsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.query.Query;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final PostsRepository  postRepository;
    private final jwtService jwtService;

    public boolean compareUsername(HttpServletRequest request, String authorid){
        String authorizationHeader = request.getHeader("Authorization");
        // String encodedCredentials = authorizationHeader.substring(7);
        String token=authorizationHeader.substring(7);
        String username=jwtService.extractUsername(token);
        return authorid.equals(username);

    }

    public String getuser(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        // String encodedCredentials = authorizationHeader.substring(7);
        String token=authorizationHeader.substring(7);
        String username=jwtService.extractUsername(token);
        return username;

    }
    @CacheEvict(value = "commentCache", key="#postId")
    public void createComment(CommentRequest commentRequest,String postId,HttpServletRequest request) throws Exception{
        String username=getuser(request);
        Optional<Post> verify_post = postRepository.findById(postId);
        if (verify_post==null){
            throw new Exception("comment can't be added wrong post id");
        }
        if(commentRequest.getContent().length()==0){
            throw new Exception("you can't post an empty comment");
        }

        Comment comment=Comment.builder()
                .postid(postId)
                .author(username)
                .content(commentRequest.getContent())
                .numb_likes(0)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now()).build();
        commentsRepository.save(comment);
        incrementComments(postId);
        log.info("comment{}is saved",comment.getId());

    }
    @Cacheable(value = "commentCache", key="#postId")
    public List<CommentResponse> getALLComments(String postId) {
        List<Comment> comments=getAllCommentsByPostId(postId);

        return comments.stream().map(this::mapToCommentResponse).toList();
        // return commentsRepository.findByPostId(postId);

    }
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Comment> getAllCommentsByPostId(String postId){
        return commentsRepository.findBypostid(postId);
    }
    public CommentResponse getCommentById(String id) {
        Optional<Comment> comments=commentsRepository.findById(id);
        return comments.map(this::mapToCommentResponse).get();
    }
    @CacheEvict(value = "commentCache", key = "#id")
    public void updateComment(String id, String newContent,HttpServletRequest request) throws Exception {
        Comment comment = commentsRepository.findById(id).orElse(null);

        if (comment != null) {
            boolean check =compareUsername(request,comment.getAuthor());
            if(!check){
                throw new Exception("Wrong user");
            }
            if (newContent != null && newContent.length() != 0) {
                comment.setContent(newContent);
            }

            comment.setUpdatedAt(java.time.LocalDateTime.now());
            commentsRepository.save(comment);
        }
    }
    @CacheEvict(value = "commentCache", key = "#postId")
    public void deleteComment(String id, String postId,HttpServletRequest request) throws Exception {
        // String username=getuser(request);
        Comment comment = commentsRepository.findById(id).orElse(null);
        if(comment != null) {
            boolean check =compareUsername(request,comment.getAuthor());
            if(!check){
                throw new Exception("Wrong user");
            }
            commentsRepository.deleteById(id);
            decrementComments(postId);
        }
    }
    @CacheEvict(value = "commentCache", key = "#postId")
    public void deleteAllCommentsByPostId(String postId){
        commentsRepository.deleteBypostid(postId);
    }
    public void incrementLikes(String id) {
        Comment comment = commentsRepository.findById(id).orElse(null);
        if (comment!= null) {
            comment.setNumb_likes(comment.getNumb_likes() + 1);
            commentsRepository.save(comment);
        }
    }

    public void decrementLikes(String id) {
        Comment comment= commentsRepository.findById(id).orElse(null);
        if (comment != null) {
            int LikesCount = comment.getNumb_likes();
            if (LikesCount > 0) {
                comment.setNumb_likes(comment.getNumb_likes() - 1);
                commentsRepository.save(comment);
            }
        }
    }
    private CommentResponse mapToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postid(comment.getPostid())
                .author(comment.getAuthor())
                .content(comment.getContent())
                .numb_likes(comment.getNumb_likes())
                .createdAt(comment.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
                .updatedAt(comment.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS))
                .build();

    }
    public void incrementComments(String id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setCommentCount(post.getCommentCount() + 1);
            postRepository.save(post);
        }
    }

    public void decrementComments(String id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            int commentCount = post.getCommentCount();
            if (commentCount > 0) {
                post.setCommentCount(post.getCommentCount() - 1);
                postRepository.save(post);
            }
        }
    }
}

