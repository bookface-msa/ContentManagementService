package com.bookface.postsservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsResponse implements Serializable {
    private String id;
    private String author_id;
    private String title;
    private String body;
    private List<String> categoryNames;
    private String photoURL;
    private int claps;
    private int commentsCount;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
