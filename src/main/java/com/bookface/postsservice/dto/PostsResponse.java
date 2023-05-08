package com.bookface.postsservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsResponse {
    private String id;
    private String author_id;
    private String title;
    private String subTitle;
    private String body;
    private String photoURL;
    private int claps;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




}
