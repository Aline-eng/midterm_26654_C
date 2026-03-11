package com.farmco.farmco_connect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmco.farmco_connect.model.User;
import com.farmco.farmco_connect.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody User user, @RequestParam String village) {
        String response = userService.saveUser(user, village);

        if (response.equals("User saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        String response = userService.login(username, password);

        if (response.equals("Login successful")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "/by-province", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsersByProvince(@RequestParam String province) {
        List<User> users = userService.getUsersByProvince(province);
        if (users.isEmpty()) {
            return new ResponseEntity<>("No users found for this province", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/by-location", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsersByLocation(@RequestParam String value) {
        List<User> users = userService.getUsersByProvince(value);
        if (users.isEmpty()) {
            return new ResponseEntity<>("No users found for this location", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/paged", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<User>> getUsersPaginatedAndSorted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<User> users = userService.getUsersPaginatedAndSorted(page, size, sortBy, direction);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/assign-farmer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignFarmerToUser(@RequestParam String userId, @RequestParam String farmerId) {
        String response = userService.assignFarmerToUser(userId, farmerId);
        if (response.equals("Farmer assigned to user successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
