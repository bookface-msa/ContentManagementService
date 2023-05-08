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
public class PostsRequest {
    private String author_id;
    private String title;
    private String subTitle;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
