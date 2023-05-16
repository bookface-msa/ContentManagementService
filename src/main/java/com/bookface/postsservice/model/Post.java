package com.bookface.postsservice.model;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document(value = "post")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class Post {
    @Id
    private String id;
    private String author_id;
    private String title;
    private String body;
    private String photoURL;
    private List<String> categoryNames;
    private int claps;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
