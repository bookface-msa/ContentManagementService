package com.bookface.postsservice.service;

import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.dto.PostsResponse;
import com.bookface.postsservice.model.Post;
import com.bookface.postsservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;


    public void createPost(PostsRequest postsRequest) throws Exception{
        if(postsRequest.getTitle() == null || postsRequest.getTitle().length() == 0){
            throw new Exception("Post is missing a Title");
        }
        if(postsRequest.getBody() == null || postsRequest.getBody().length() == 0){
            throw new Exception("Post cannot be empty");
        }

        //TODO: get authorID from token or mq? and save image to mediaserver to put photoURL.

        Post post = Post.builder()
                .title(postsRequest.getTitle())
                .subTitle(postsRequest.getSubTitle())
                .body(postsRequest.getBody())
                .claps(0)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        postsRepository.insert(post);
        log.info("Post {} Saved", post.getId());
    }
    public List<PostsResponse> getAllPosts(){
        List<Post> posts = postsRepository.findAll();

        return posts.stream().map(this::mapToPostResponse).toList();
    }

    public PostsResponse getPostById(String id) {
        Optional<Post> post = postsRepository.findById(id);
        return post.map(this::mapToPostResponse).get();
    }

    public void updatePost(String id, String newTitle, String newBody){
        Post post = postsRepository.findById(id).orElse(null);
        //TODO: Check if the current session userId matches the posts authorID
        if(post!=null) {
            if(newTitle!=null && newTitle.length() !=0) {
                post.setTitle(newTitle);
            }
            if(newBody!=null && newBody.length() !=0) {
                post.setBody(newBody);
            }
            post.setUpdatedAt(java.time.LocalDateTime.now());
            postsRepository.save(post);
        }
    }

    public void clap(String id){
        Post post = postsRepository.findById(id).orElse(null);
        System.out.println(post.getId());
        if(post!=null) {
            post.setClaps(post.getClaps() + 1);
            postsRepository.save(post);
        }
    }

    private PostsResponse mapToPostResponse(Post post) {
        return PostsResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .subTitle(post.getSubTitle())
                .body(post.getBody())
                .claps(post.getClaps())
                .photoURL(post.getPhotoURL())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }


//    public Post getPostById(Long id) {
////        Optional<Post> postOptional = postsRepository.findById(id);
//    }

}
