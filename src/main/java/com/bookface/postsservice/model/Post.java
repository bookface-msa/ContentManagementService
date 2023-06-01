package com.bookface.postsservice.model;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field(name = "author_id")
    private String authorId;
    private String title;
    private String body;
    private List<String> categoryNames;
    private String photoURL;
    private int claps;
    private int commentCount;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //TODO id,authorId,title,body,createdAt,updatedAt,tags send to elastic search using MQ on create/update/delete
}
