package org.example.service;

import org.example.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional(readOnly = true)
    public List<User> getAllUser() {
        List<User> result = (List<User>) userRepository.findAll();
        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<User>();
        }
    }
    @Transactional(readOnly = true)
    public Optional<User>getUserByEmail(String name) {
        return userRepository.findByEmail(name);
    }
    @Transactional
    public void createOrUpdateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            System.out.println("Такого задания нет");
        }
    }

}
