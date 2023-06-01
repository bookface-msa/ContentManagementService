package com.bookface.postsservice.model;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(value = "category")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class Category {
    @Id
    private String id;
    private String categoryName;
    private int categoryCount;
}

