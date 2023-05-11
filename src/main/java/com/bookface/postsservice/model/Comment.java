package com.bookface.postsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Comment {
@Id
    private String id;
    private String post_id;
    private String author;
    private String content;
    private int numb_likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
