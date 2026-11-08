package com.project.ecommerce_app.service;

import com.project.ecommerce_app.entity.Product;
import com.project.ecommerce_app.entity.ProductImage;
import com.project.ecommerce_app.repository.ProductImageRepository;
import com.project.ecommerce_app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    private final String UPLOAD_DIR = "uploads/"; // Ensure this folder exists or use dynamic path

    @Override
    public List<ProductImage> saveImage(Integer productId, MultipartFile[] files) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String uploadDir = new File("uploads").getAbsolutePath();
        File uploadFolder = new File(uploadDir);

        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        List<ProductImage> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                File destination = new File(uploadFolder, filename);

                try {
                    file.transferTo(destination);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }

                ProductImage image = new ProductImage();
                image.setImage_url("/" + UPLOAD_DIR + filename);
                image.setProduct(product);

                savedImages.add(imageRepository.save(image));
            }
        }

        return savedImages;
    }

    @Override
    public List<ProductImage> getImagesByProductId(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        return imageRepository.findByProduct(product);
    }

    @Override
    public void deleteImage(Integer imageId) {
        imageRepository.deleteById(imageId);
    }
}
