package org.akriuchk.minishop.controller;


import org.akriuchk.minishop.config.ApiResponse;
import org.akriuchk.minishop.model.UserProfile;
import org.akriuchk.minishop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Iterable<UserProfile>> getUsers() {
        Iterable<UserProfile> dtos = userService.listProfiles();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> newUser(@RequestBody UserProfile profile) {
        userService.addProfile(profile);
        return new ResponseEntity<>(new ApiResponse(true, "Profile has been created."), HttpStatus.CREATED);
    }
}
