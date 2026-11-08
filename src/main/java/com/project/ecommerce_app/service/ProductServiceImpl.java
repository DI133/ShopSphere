package com.project.ecommerce_app.service;

import com.project.ecommerce_app.entity.Category;
import com.project.ecommerce_app.entity.Product;
import com.project.ecommerce_app.entity.User;
import com.project.ecommerce_app.entity.ProductImage;
import com.project.ecommerce_app.repository.CartItemRepository;
import com.project.ecommerce_app.repository.CategoryRepository;
import com.project.ecommerce_app.repository.ProductRepository;
import com.project.ecommerce_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public Product createProduct(Product product){
        return productRepository.save(product);
    }
    @Override
    @Transactional
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }


    @Override
    @Transactional
    public Optional<Product> updateProduct(
            Integer id,
            String name,
            String description,
            Double price,
            Integer stock,
            Integer categoryId,
            MultipartFile[] images
    ) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        Product updatedProduct = existingProduct.get();

        updatedProduct.setName(name);
        updatedProduct.setDescription(description);
        updatedProduct.setPrice(price);
        updatedProduct.setStock(stock);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        updatedProduct.setCategory(category);

        if (images != null && images.length > 0) {
            try {
                String uploadDir = new File("uploads").getAbsolutePath();
                File uploadFolder = new File(uploadDir);

                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                if (updatedProduct.getImages() == null) {
                    updatedProduct.setImages(new ArrayList<>());
                } else {
                    updatedProduct.getImages().clear();
                }

                for (MultipartFile image : images) {
                    if (image != null && !image.isEmpty()) {
                        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
                        File destination = new File(uploadFolder, filename);
                        image.transferTo(destination);

                        String imageUrl = "/uploads/" + filename;

                        ProductImage productImage = new ProductImage();
                        productImage.setImage_url(imageUrl);
                        productImage.setProduct(updatedProduct);

                        updatedProduct.getImages().add(productImage);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload images", e);
            }
        }

        return Optional.of(productRepository.save(updatedProduct));
    }


    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // delete related cart items first
        cartItemRepository.deleteByProduct(product);

        // then delete product
        productRepository.delete(product);
    }


    @Transactional
    @Override
    public List<Product> getProductByUserId(Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return productRepository.findByUser(user);

    }

}
