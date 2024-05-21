package org.example.controller;


import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<CustomResponse> getAllUsers() {

        if (userRepository.count() < 1) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("No data found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }


        CustomResponse response = new CustomResponse("success", this.userRepository.findAll());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getUserByID(@PathVariable int id) {
        if (!userRepository.existsById(id)) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("Id is not in database!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!"));
        CustomResponse res = new CustomResponse("Success", user);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<CustomResponse> createUser(@RequestBody User user) {

        if (user.getUsername() == null) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("Check if all fields are correct!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }
        CustomResponse res = new CustomResponse("success", this.userRepository.save(user));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> deleteUserByID(@PathVariable int id) {

        if (!userRepository.existsById(id)) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("Id is not in database!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }

        User user = this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!"));
        this.userRepository.delete(user);

        CustomResponse res = new CustomResponse("success", user);
        return ResponseEntity.ok(res);


    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse> updateMovie(@PathVariable int id, @RequestBody User user) {


        User prevUser = this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found!"));
        User userToUpdate = this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!"));

        if (user.getUsername() == null) {
            userToUpdate.setUsername(prevUser.getUsername());
        }
        else {
            userToUpdate.setUsername(user.getUsername());
        }

        CustomResponse res = new CustomResponse("success", this.userRepository.save(userToUpdate));

        return ResponseEntity.ok(res);
    }

}
