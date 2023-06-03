package com.bookface.postsservice.controller;

import com.bookface.postsservice.exceptions.BadRequestException;
import com.bookface.postsservice.service.CategoriesService;
//import com.google.api.gax.grpc.ResponseMetadataHandler;
//import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content//api/category")
@RequiredArgsConstructor
public class CategoriesController {

    @Autowired
    private final CategoriesService categoriesService;



    @PostMapping
    public ResponseEntity<String> createCategory(List<String> categoriesNames) throws Exception{
        System.out.println(categoriesNames);
        System.out.println("categories namess");
        if(categoriesNames.size() > 5){
            throw new BadRequestException("Maximum number of categories is 5");
        }
        else {
            try {
                categoriesService.createCategory(categoriesNames);
                return ResponseEntity.status(HttpStatus.CREATED).body("Category created successfuly");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }
}