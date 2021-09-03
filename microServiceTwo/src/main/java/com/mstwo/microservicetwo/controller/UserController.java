package com.mstwo.microservicetwo.controller;

import com.mstwo.microservicetwo.Util.UserEntity;
import com.mstwo.microservicetwo.Util.UserRepository;
import com.mstwo.microservicetwo.Util.UserRequestModel;
import com.mstwo.microservicetwo.Util.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;

@RestController
@RequestMapping("users")
public class UserController implements Serializable {
    @Autowired
    UserRepository repository;
    final String secretKey = "test123Auth@1234";

    @PostMapping( value = "/storeData",consumes = MediaType.APPLICATION_JSON_VALUE,produces = {MediaType.APPLICATION_XML_VALUE,"text/csv"} )
    public ResponseEntity<UserResponse> storeData(@Valid @RequestBody UserRequestModel userRequest, @RequestHeader(name = "fileType") String fileType) throws IOException {
        UserResponse response = new UserResponse();
        response.setName(userRequest.getName());
        response.setDob(userRequest.getDob());
        response.setAge(userRequest.getAge());
        response.setSalary(userRequest.getSalary());
        UserEntity userDetail = new UserEntity();
        userDetail.setAge(response.getAge());
        userDetail.setDob(response.getDob());
        userDetail.setName(response.getName());
        userDetail.setSalary(response.getSalary());
        final UserEntity storeUser = repository.save(userDetail);
        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);

    }
    @PutMapping(value = "/updateData/{name}",consumes = {MediaType.APPLICATION_JSON_VALUE},produces = { MediaType.APPLICATION_XML_VALUE,"text/csv"} )
    ResponseEntity<UserEntity> updateData( @PathVariable(value = "name") String userName,@Valid @RequestBody UserRequestModel userRequest, @RequestHeader(name = "fileType") String fileType) throws Exception {
        UserEntity name = (UserEntity) repository.findByName(userName);
        UserResponse response = new UserResponse();
        response.setName(name.toString());
        response.setDob(userRequest.getDob());
        response.setAge(userRequest.getAge());
        response.setSalary(userRequest.getSalary());
        final UserEntity updatedUser = repository.save(name);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping(value = "/readData",consumes = {MediaType.APPLICATION_JSON_VALUE},produces = { MediaType.APPLICATION_XML_VALUE,"text/csv"} )
    ResponseEntity<UserEntity> readData(@PathVariable(value = "name") String userName,@Valid @RequestBody UserRequestModel user ) throws Exception {
        UserEntity name = (UserEntity) repository.findByName(userName);
        return ResponseEntity.ok().body(name);
    }
}