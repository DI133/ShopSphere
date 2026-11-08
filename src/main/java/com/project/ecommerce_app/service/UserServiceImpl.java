package com.project.ecommerce_app.service;
import com.project.ecommerce_app.dto.CustomerDetailsDTO;
import com.project.ecommerce_app.dto.CustomerPurchaseDTO;
import com.project.ecommerce_app.dto.SellerDetailsDTO;
import com.project.ecommerce_app.dto.SellerProductDTO;
import com.project.ecommerce_app.entity.OrderItem;
import com.project.ecommerce_app.entity.Product;
import com.project.ecommerce_app.entity.User;
import com.project.ecommerce_app.entity.Role;

import com.project.ecommerce_app.repository.OrderItemRepository;
import com.project.ecommerce_app.repository.ProductRepository;
import com.project.ecommerce_app.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Integer userId, User user) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }


    @Override
    public List<User> getAllSellers() {
        return userRepository.findByRole(Role.seller);
    }

    @Override
    public List<User> getAllCustomers() {
        return userRepository.findByRole(Role.customer);
    }

    @Override
    public List<SellerDetailsDTO> getSellerDetails() {
        List<User> sellers = userRepository.findByRole(Role.seller);

        return sellers.stream().map(seller -> {
            List<Product> products = productRepository.findByUser(seller);

            List<SellerProductDTO> productDTOs = products.stream()
                    .map(product -> new SellerProductDTO(
                            product.getProduct_id(),
                            product.getName(),
                            product.getPrice()
                    ))
                    .toList();

            Set<String> categorySet = products.stream()
                    .filter(product -> product.getCategory() != null)
                    .map(product -> product.getCategory().getName())
                    .collect(Collectors.toSet());

            return new SellerDetailsDTO(
                    seller.getUser_id(),
                    seller.getUsername(),
                    seller.getEmail(),
                    categorySet.stream().toList(),
                    productDTOs,
                    products.size(),
                    categorySet.size()
            );
        }).toList();
    }

    @Override
    public List<CustomerDetailsDTO> getCustomerDetails() {

        List<User> customers = userRepository.findByRole(Role.customer);

        return customers.stream().map(customer -> {

            List<OrderItem> items = orderItemRepository.findByOrder_User(customer);

            List<CustomerPurchaseDTO> purchases = items.stream()
                    .map(item -> new CustomerPurchaseDTO(
                            item.getProduct().getName(),
                            item.getProduct().getUser().getUsername()
                    ))
                    .toList();

            return new CustomerDetailsDTO(
                    customer.getUser_id(),
                    customer.getUsername(),
                    customer.getEmail(),
                    purchases,
                    purchases.size()
            );

        }).toList();
    }

}
