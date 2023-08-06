package io.incondensable.application.web.controllers;

import io.incondensable.application.business.domain.entity.User;
import io.incondensable.application.business.service.user.UserService;
import io.incondensable.application.web.dto.req.user.UserCreateRequestDTO;
import io.incondensable.application.web.dto.req.user.UserUpdateRequestDTO;
import io.incondensable.application.web.dto.res.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author abbas
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Validated @RequestBody UserCreateRequestDTO req) {
        return ResponseEntity.ok(userService.createUser(req));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String userId, @Validated @RequestBody UserUpdateRequestDTO req) {
        return ResponseEntity.ok(userService.updateUser(userId, req));
    }

}
