package com.example.demo.controller;

import com.example.demo.entity.SystemUser;
import com.example.demo.repository.SystemUserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://padmanaphan.github.io")
public class UserController {

    private final SystemUserRepository userRepository;

    public UserController(SystemUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<SystemUser> getAllUsers() {
        return userRepository.findAll();
    }
}
