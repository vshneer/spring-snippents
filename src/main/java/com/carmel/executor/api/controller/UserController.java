package com.carmel.executor.api.controller;

import com.carmel.executor.api.entity.User;
import com.carmel.executor.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    private UserService service;
    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    private ResponseEntity saveUsers(@RequestParam(value = "file") MultipartFile[] files) throws Exception {
        for(MultipartFile file:files){
            service.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/usersAsync", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    private ResponseEntity saveUsersAsync(@RequestParam(value = "file") MultipartFile[] files) {
        for(MultipartFile file:files){
            service.saveUsersAsync(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers(){
        return service.findAllUsers().thenApply(ResponseEntity::ok);
    }
    @GetMapping(value = "/getUsersByThread", produces = "application/json")
    public ResponseEntity getUsers(){
        CompletableFuture<List<User>> users1 = service.findAllUsers();
        CompletableFuture<List<User>> users2 = service.findAllUsers();
        CompletableFuture<List<User>> users3 = service.findAllUsers();
        CompletableFuture.allOf(users1,users3,users2).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}