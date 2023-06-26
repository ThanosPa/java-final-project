package com.booleanuk.recipes.controller;

import com.booleanuk.recipes.model.RecipeImage;
import com.booleanuk.recipes.repository.RecipeImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("images")
public class ImageController {

    @Autowired
    private RecipeImageRepository recipeImageRepository;
    @GetMapping("/{imageUrl}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageUrl) {
        // Retrieve the image byte array based on the imageId from the database or storage location
        Optional<RecipeImage> recipeImage = recipeImageRepository.findByImageUrl(imageUrl);
        if(recipeImage.isPresent()){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(recipeImage.get().getImage(), headers, HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
