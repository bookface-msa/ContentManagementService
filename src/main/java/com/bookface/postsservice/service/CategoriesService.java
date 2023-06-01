package com.bookface.postsservice.service;
import com.bookface.postsservice.model.Category;
import com.bookface.postsservice.mqconfig.MessagingConfig;
import com.bookface.postsservice.repository.CategoriesRepository;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor

public class CategoriesService{
    @Autowired
    private final CategoriesRepository categoriesRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;

    public Category findCategoryByCategoryName(String categoryName) {
        return this.categoriesRepository.findCategoryByCategoryName(categoryName);
    }

    public Category findCategoryByCategoryId(String categoryId) {
        return this.categoriesRepository.findCategoryById(categoryId);
    }

    public void deleteCategoryByCategoryName(String categoryName) {
        Category category = this.categoriesRepository.findCategoryByCategoryName(categoryName);
        if(category != null){
            this.categoriesRepository.delete(category);
        }

    }

    public void saveCategory(Category category){
        this.categoriesRepository.save(category);
    }

    public void createCategory(List<String> categoriesNames){
        for(String categoryName : categoriesNames){
            Category category = findCategoryByCategoryName(categoryName);
            if (category != null) {
                category.setCategoryCount(category.getCategoryCount() + 1);
            } else {
                category = Category.builder()
                        .categoryName(categoryName)
                        .categoryCount(1)
                        .build();
                String message = "Category created: " + categoryName;
                rabbitTemplate.convertAndSend(MessagingConfig.CATEGORIES_EXCHANGE, MessagingConfig.CATEGORIES_ROUTING_KEY_CREATE, message);
            }
            saveCategory(category);

        }
    }

    public void deleteCategoryOrReduceCount(List<String> categoryNames){
        for(String categoryName: categoryNames){
            Category category = findCategoryByCategoryName(categoryName);
            if(category.getCategoryCount() == 1){
                categoriesRepository.deleteByCategoryName(categoryName);
                String message = "Category deleted: " + categoryName;
                rabbitTemplate.convertAndSend(MessagingConfig.CATEGORIES_EXCHANGE, MessagingConfig.CATEGORIES_ROUTING_KEY_DELETE, message);
            }
            else{
                category.setCategoryCount(category.getCategoryCount() - 1);
                categoriesRepository.save(category);
            }
        }
    }
}