package com.msone.microserviceone.Controller;

import com.msone.microserviceone.Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

@RestController
@RequestMapping("users")
public class UserController implements Serializable {
    @Autowired
    UserRepository repository;

    @Autowired
    RestTemplate restTemplate;

    final String secretKey = "test123Auth@1234";

    @PostMapping(value = "/storeData", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.APPLICATION_XML_VALUE, "text/csv"})
    public String storeData(@Valid @RequestBody UserRequestModel userRequest, @RequestHeader(name = "fileType") String fileType) throws IOException {
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
        String encryptedString = AES.encrypt(userDetail.toString(), secretKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<UserEntity> entity = new HttpEntity<UserEntity>(storeUser, headers);

        return restTemplate.exchange(
                "http://localhost:8080/storeData", HttpMethod.POST, entity, String.class).getBody();
    }
    //return new ResponseEntity<UserResponse>(response, HttpStatus.OK);



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