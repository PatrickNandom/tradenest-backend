package com.tradenest.service;

import com.tradenest.entity.User;
import com.tradenest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, S3Client s3Client) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        User existingUser = findUserById(id);

        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
                userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));

        return userRepository.save(existingUser);
    }
    @PreAuthorize("hasRole('Admin')")
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }


    public String uploadProfileImage(UUID userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            // Create unique S3 key (path + filename)
            String key = "profile-images/" + userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Build PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // Build the public URL to return and save in DB
            String imageUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;

            // Save URL in user entity
            user.setImageUri(imageUrl);
            userRepository.save(user);

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
}
