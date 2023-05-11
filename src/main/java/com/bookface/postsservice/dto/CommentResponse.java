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
public class CommentResponse {
    private String post_id;
    private String author;
    private String content;
    private int numb_likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
