package com.bookface.postsservice.service;

import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.dto.PostsResponse;
import com.bookface.postsservice.firebase.FirebaseInterface;
import com.bookface.postsservice.model.Post;
import com.bookface.postsservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;

    @Autowired
    private final FirebaseInterface IFirebase;


    public void createPost(PostsRequest postsRequest) throws Exception {
        if (postsRequest.getTitle() == null || postsRequest.getTitle().length() == 0) {
            throw new Exception("Post is missing a Title");
        }
        if (postsRequest.getBody() == null || postsRequest.getBody().length() == 0) {
            throw new Exception("Post cannot be empty");
        }

        //TODO: get authorID from token or mq?

        Post post = Post.builder()
                .title(postsRequest.getTitle())
                .subTitle(postsRequest.getSubTitle())
                .body(postsRequest.getBody())
                .claps(0)
                .commentCount(0)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        //Save image to firebase and save image url.
        try {
            MultipartFile file = postsRequest.getFile();
            if (file != null) {
                String fileName = IFirebase.save(file);
                String imageUrl = IFirebase.getImageUrl(fileName);
                post.setPhotoURL(imageUrl);
                System.out.println(imageUrl);
            }

        } catch (Exception e) {
            throw new Exception("Failed to upload Image");
        }

        postsRepository.insert(post);
        log.info("Post {} Saved", post.getId());
    }

    public List<PostsResponse> getAllPosts() {
        List<Post> posts = postsRepository.findAll();

        return posts.stream().map(this::mapToPostResponse).toList();
    }

    public PostsResponse getPostById(String id) {
        Optional<Post> post = postsRepository.findById(id);
        return post.map(this::mapToPostResponse).get();
    }

    public void updatePost(String id, String newTitle, String newBody) {
        Post post = postsRepository.findById(id).orElse(null);
        //TODO: Check if the current session userId matches the posts authorID
        if (post != null) {
            if (newTitle != null && newTitle.length() != 0) {
                post.setTitle(newTitle);
            }
            if (newBody != null && newBody.length() != 0) {
                post.setBody(newBody);
            }
            post.setUpdatedAt(java.time.LocalDateTime.now());
            postsRepository.save(post);
        }
    }

    public void clap(String id) {
        Post post = postsRepository.findById(id).orElse(null);
        if (post != null) {
            post.setClaps(post.getClaps() + 1);
            postsRepository.save(post);
        }
    }

    public void incrementComments(String id) {
        Post post = postsRepository.findById(id).orElse(null);
        if (post != null) {
            post.setCommentCount(post.getCommentCount() + 1);
            postsRepository.save(post);
        }
    }

    public void decrementComments(String id) {
        Post post = postsRepository.findById(id).orElse(null);
        if (post != null) {
            int commentCount = post.getCommentCount();
            if (commentCount > 0) {
                post.setCommentCount(post.getCommentCount() - 1);
                postsRepository.save(post);
            }
        }
    }

    private PostsResponse mapToPostResponse(Post post) {
        return PostsResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .subTitle(post.getSubTitle())
                .body(post.getBody())
                .claps(post.getClaps())
                .commentsCount(post.getCommentCount())
                .photoURL(post.getPhotoURL())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }


}
