package com.project.ecommerce_app.controller;

import com.project.ecommerce_app.entity.ProductImage;
import com.project.ecommerce_app.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    @Autowired
    private ProductImageService imageService;

    // add images to product
    @PostMapping("/{productId}")
    public ResponseEntity<List<ProductImage>> uploadImages(
            @PathVariable Integer productId,
            @RequestParam("images") MultipartFile[] images
    ) {
        return ResponseEntity.ok(imageService.saveImage(productId, images));
    }

    // get images of product
    @GetMapping("/{productId}")
    public ResponseEntity<List<ProductImage>> getImagesByProductId(@PathVariable Integer productId) {
        return ResponseEntity.ok(imageService.getImagesByProductId(productId));
    }

    // delete image of product
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}

