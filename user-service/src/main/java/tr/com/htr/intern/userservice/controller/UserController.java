package tr.com.htr.intern.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.htr.intern.userservice.dto.*;
import tr.com.htr.intern.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

//    @GetMapping("")
//    public String viewHomePage() {
//        return "index";
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<UserResponse>(userService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword()), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                          @ModelAttribute FilterRequest filterRequest) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(userService.getAllUsers(filterRequest, pageRequest), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/user/{id}/permission")
    public ResponseEntity<List<PermissionResponse>> findPermissionsByUserId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findPermissionsByUsersId(id), HttpStatus.OK);
    }

    @GetMapping("/permission/{id}/user")
    public ResponseEntity<Page<UserResponse>> findUsersByPermissionsId(@PathVariable("id") Long id,
                                                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(userService.findUsersByPermissionsId(id, pageRequest), HttpStatus.FOUND);
    }

    @GetMapping("/permission/{id}/user/{user_id}")
    public ResponseEntity<UserResponse> findUserByPermissionsId(@PathVariable("id") Long id, @PathVariable("user_id") Long user_id) {
        return new ResponseEntity<>(userService.findUserByPermissionsId(id, user_id), HttpStatus.FOUND);
    }

    @DeleteMapping("/user/{id}")
    public HttpStatus deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return HttpStatus.OK;
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserCreateRequest userCreateRequest) {
        return new ResponseEntity<>(userService.createUser(userCreateRequest), HttpStatus.CREATED);
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest user) {
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

}
