package com.bookface.postsservice.model;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


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
    private String subTitle;
    private String body;
    private String photoURL;
    private int claps;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


//    public Post(String id, String title, String subTitle, Date createdAt,Date updatedAt, String body){
//        this.id = id;
//        this.title = title;
//        this.subTitle = subTitle;
//        this.body = body;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//    public String getId() {
//        return id;
//    }

//    public String getTitle() {
//        return title;
//    }
//
//    public String getSubTitle() {
//        return subTitle;
//    }
//
//    public LocalDateTime getCreationDate() {
//        return createdAt;
//    }
//    public LocalDateTime getUpdateDate() {
//        return updatedAt;
//    }
//
//    public String getBody() {
//        return body;
//    }

}
