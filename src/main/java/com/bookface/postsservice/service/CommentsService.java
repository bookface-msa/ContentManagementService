package com.bookface.postsservice.service;

import com.bookface.postsservice.dto.CommentRequest;
import com.bookface.postsservice.dto.CommentResponse;
import com.bookface.postsservice.model.Comment;
import com.bookface.postsservice.model.Post;
import com.bookface.postsservice.repository.CommentsRepository;
import com.bookface.postsservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

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



    public void createComment(CommentRequest commentRequest,String postId) throws Exception{
        Optional<Post> verify_post = postRepository.findById(postId);
        if (verify_post==null){
            throw new Exception("comment can't be added wrong post id");
    }
        if(commentRequest.getContent().length()==0){
            throw new Exception("you can't post an empty comment");
        }

        Comment comment=Comment.builder()
                .postid(postId)
                .author(commentRequest.getAuthor())
                .content(commentRequest.getContent())
                .numb_likes(0)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now()).build();
        commentsRepository.save(comment);
        log.info("comment{}is saved",comment.getId());

    }
    @Cacheable(value = "commentCache", key = "#id")
    public List<CommentResponse> getALLComments(String postId) {
        List<Comment> comments=getAllCommentsByPostId(postId);
        System.out.println(comments);

      return comments.stream().map(this::mapToCommentResponse).toList();
       // return commentsRepository.findByPostId(postId);

    }
    @Autowired
    private MongoTemplate mongoTemplate;
    public List<Comment> getAllCommentsByPostId(String postId){

//        Query query=new Query(Criteria.where("post_id").is(postId));
//       List<Comment> comments =  mongoTemplate.find(query, Comment.class);
       // List<Comment> comments = commentsRepository.findBy()
//
//        List<Comment> comments = mongoTemplate.find(
//                Query.query(Criteria.where("post_id").is(postId)),
//                Comment.class
//        );

        return commentsRepository.findBypostid(postId);
       // System.out.println(comments);

       // return comments;
    }
    /*
    public List<Comment> getAllCommentsOfPost(String postId){
        List<Comment> comments=commentsRepository.findAll();
        List<Comment> returned_list=new ArrayList<>();
        for(int i=0;i<comments.size();i++){
            if(comments.get(i).getPost_id().equals(postId)){
                  returned_list.add(comments.get(i));
            }

        }
       return returned_list;
    }*/
    public CommentResponse getCommentById(String id) {
        Optional<Comment> comments=commentsRepository.findById(id);
        return comments.map(this::mapToCommentResponse).get();
    }
    @CacheEvict(value = "commentCache", key = "#id")
    public void updateComment(String id, String newContent) {
        Comment comment = commentsRepository.findById(id).orElse(null);

        if (comment != null) {
            if (newContent != null && newContent.length() != 0) {
                comment.setContent(newContent);
            }

            comment.setUpdatedAt(java.time.LocalDateTime.now());
            commentsRepository.save(comment);
        }
    }
        public void deleteComment(String id) {
            Comment comment = commentsRepository.findById(id).orElse(null);
            if(comment != null) {
                commentsRepository.deleteById(id);

            }
        }
    @CacheEvict(value = "commentCache", key = "#id")
        public void deleteAllCommentsByPostId(String postId){
           commentsRepository.deleteBypostid(postId);
            /*
           for(int i=0;i<commentsToDelete.size();i++){
               commentsRepository.deleteById(commentsToDelete.get(i).getId());
           }*/

          //  return

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
                .createdAt(comment.getCreatedAt())
                .build();

    }
    }

