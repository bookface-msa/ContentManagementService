package com.bookface.postsservice;

import com.bookface.postsservice.dto.PostsRequest;
import com.bookface.postsservice.repository.PostsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class PostsServiceApplicationTests {
//
//    @Container
//    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.5");
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private PostsRepository postsRepository;

//    @DynamicPropertySource
//    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
//        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//    }

//    @Test
//    void shouldCreatePost() throws Exception {
//        PostsRequest postsRequest = getPostRequest();
//        String postRequestString = objectMapper.writeValueAsString(postsRequest);
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(postRequestString))
//                .andExpect(status().isCreated());
//        Assertions.assertTrue(postsRepository.findAll().size() == 1);
////        try {
////            Assertions.assertTrue(postsRepository.findAll().size() == 2);
////        } catch (AssertionError e) {
////            System.out.println("Only" + postsRepository.findAll().size() + "posts were created");
////        }
//    }
//
//    private PostsRequest getPostRequest() {
//        return PostsRequest.builder()
//                .title("Test Post")
//                .body("This is a test Post")
//                .createdAt(java.time.LocalDateTime.now())
//                .updatedAt(java.time.LocalDateTime.now())
//                .build();
//    }

}
