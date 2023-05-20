package com.bookface.postsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse implements Serializable {
    private String id;
    private String postid;
    private String author;
    private String content;
    private int numb_likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
