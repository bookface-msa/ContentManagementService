package com.bookface.postsservice.repository;
import com.bookface.postsservice.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CategoriesRepository extends MongoRepository<Category, String> {
    Category findCategoryByCategoryName(String categoryName);

    Category findCategoryById(String categoryId);

    void deleteByCategoryName(String categoryName);
};