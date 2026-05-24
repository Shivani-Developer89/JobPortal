package com.JobPortal.JPP.controller;


import com.JobPortal.JPP.dto.request.RegisterInputDTO;
import com.JobPortal.JPP.dto.response.RegisterOutputDTO;
import com.JobPortal.JPP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RestControllerAdvice
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping()
    public ResponseEntity<RegisterOutputDTO> addUser(@RequestBody RegisterInputDTO registerInputDTO){
        return  new ResponseEntity<>(userService.createUser(registerInputDTO), HttpStatusCode.valueOf(201));

    }

    @GetMapping("/{id}")
    public  ResponseEntity<RegisterOutputDTO> getUser(@PathVariable Long id){
        return  new ResponseEntity<>(userService.getUserById(id),HttpStatusCode.valueOf(200));
    }

    @GetMapping("/all")
    public ResponseEntity<List> getAllUser(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatusCode.valueOf(200));
    }
    @PutMapping("/{id}")
    public  ResponseEntity<RegisterOutputDTO> updateUser(@PathVariable Long id ,@RequestBody RegisterInputDTO registerInputDTO){
        return  new ResponseEntity<>(userService.updateUser(id , registerInputDTO),HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        return new ResponseEntity<>(userService.removeUser(id),HttpStatusCode.valueOf(200));
    }
}
