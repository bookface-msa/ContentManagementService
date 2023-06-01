package com.bookface.postsservice.service;

import com.bookface.postsservice.dto.ElasticPostsMessage;
import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.dto.PostsResponse;
import com.bookface.postsservice.firebase.FirebaseInterface;
import com.bookface.postsservice.model.Post;
import com.bookface.postsservice.mqconfig.MessagingConfig;
import com.bookface.postsservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsService {

    @Autowired
    private final PostsRepository postsRepository;

    @Autowired
    private final FirebaseInterface IFirebase;

    private final CommentsService commentsService;
    @Autowired
    private RabbitTemplate template;


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
                .authorId("filler")
                .body(postsRequest.getBody())
                .claps(0)
                .commentCount(0)
                .published(postsRequest.isPublished())
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        //Save image to firebase and save image url.
        try {
            List<MultipartFile> files = postsRequest.getFiles();
            System.out.println(files.size());
            if(files != null) {
                for (MultipartFile file : files) {
                    String fileName = IFirebase.save(file);
                    String imageUrl = IFirebase.getImageUrl(fileName);
                    if (post.getPhotoURL() == null) {
                        post.setPhotoURL(new ArrayList<String>());
                    }

                    post.getPhotoURL().add(imageUrl);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Failed to upload Image");
        }

        postsRepository.insert(post);

        ElasticPostsMessage postMessage = ElasticPostsMessage.builder()
                .id(post.getId())
                .authorId(post.getAuthorId())
                .createdAt(post.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
                .updatedAt(post.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS))
                .body(post.getBody())
                .title(post.getTitle())
                .build();
        template.convertAndSend(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY_CREATE,postMessage);

        log.info("Post {} Saved", post.getId());
    }


    public List<PostsResponse> getAllPosts() {
        List<Post> posts = postsRepository.findAll();

        return posts.stream().map(this::mapToPostResponse).toList();
    }

    @Cacheable(value = "postCache", key = "#id")
    public PostsResponse getPostById(String id) {
        Optional<Post> post = postsRepository.findById(id);
        return post.map(this::mapToPostResponse).get();
    }


    public List<PostsResponse> getPublishedPostsByAuthorId(String authorId) {
        List<Post> publishedPosts = postsRepository.findByAuthorIdAndPublishedTrue(authorId);
        return publishedPosts.stream().map(this::mapToPostResponse).toList();
    }
    public List<PostsResponse> getDraftedPostsByAuthorId(String authorId) {
        List<Post> draftedPosts = postsRepository.findByAuthorIdAndPublishedFalse(authorId);
        return draftedPosts.stream().map(this::mapToPostResponse).toList();
    }

    @CacheEvict(value = "postCache", key = "#id")
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
            ElasticPostsMessage postMessage = ElasticPostsMessage.builder()
                    .id(post.getId())
                    .authorId(post.getAuthorId())
                    .createdAt(post.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
                    .updatedAt(post.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS))
                    .body(post.getBody())
                    .title(post.getTitle())
                    .build();
            template.convertAndSend(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY_UPDATE,postMessage);
        }
    }

    @CacheEvict(value = "postCache", key = "#id")
    @Transactional
    public void deletePost(String id) throws Exception {
        Post post = postsRepository.findById(id).orElse(null);
        if (post != null) {
            if(post.getPhotoURL() != null) {
                for (String imageUrl : post.getPhotoURL()) {
                    try {
                        String path = new URL(imageUrl).getPath();
                        String fileName = path.substring(path.lastIndexOf('/') + 1);
                        IFirebase.delete(fileName);
                    } catch (Exception e) {
                        log.info(e.getMessage());
                    }
                }
            }

            postsRepository.deleteById(id);
            commentsService.deleteAllCommentsByPostId(id);
            template.convertAndSend(MessagingConfig.EXCHANGE,MessagingConfig.ROUTING_KEY_DELETE,post.getId());

        }else{
            throw new Exception();
        }
    }

    @CacheEvict(value = "postCache", key = "#id")
    public void clap(String id) {
        Post post = postsRepository.findById(id).orElse(null);
        if (post != null) {
            post.setClaps(post.getClaps() + 1);
            postsRepository.save(post);
        }
    }



    private PostsResponse mapToPostResponse(Post post) {
        return PostsResponse.builder()
                .id(post.getId())
                .author_id(post.getAuthorId())
                .title(post.getTitle())
                .body(post.getBody())
                .claps(post.getClaps())
                .commentsCount(post.getCommentCount())
                .photoURL(post.getPhotoURL())
                .published(post.isPublished())
                .createdAt(post.getCreatedAt().truncatedTo(ChronoUnit.SECONDS))
                .updatedAt(post.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }


}
