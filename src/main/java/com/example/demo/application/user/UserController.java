package com.example.demo.application.user;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handling REST requests for User entity.
 * Incoming object is {@link UserDto}
 *
 * @author szigeti.attila96@gmail.com
 */
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @Autowired
  UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/")
  public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto user) {
    return ResponseEntity.of(userService.createUser(user));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto user, @PathVariable Long id) {
    return ResponseEntity.of(userService.updateUser(id, user));
  }

  @GetMapping("/")
  public ResponseEntity<List<UserDto>> listAllUsers() {
    List<UserDto> dtoList = userService.listAllUsers();
    if (!dtoList.isEmpty()) {
      return ResponseEntity.ok(dtoList);
    }
    return ResponseEntity.notFound().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserInfo(@PathVariable("id") Long id) {
    return ResponseEntity.of(userService.getUserInfo(id));
  }
}
