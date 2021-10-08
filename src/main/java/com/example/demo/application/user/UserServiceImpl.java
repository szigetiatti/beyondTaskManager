package com.example.demo.application.user;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation class for {@link UserService}
 *
 * @author szigeti.attila96@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<UserDto> createUser(UserDto userDto) {
    if (userDto == null) {
      log.error("UserDto is null");
      return Optional.empty();
    }

    User newUser = deriveUserFromDto(userDto);
    Optional<User> existingUser = userRepository.findByUserName(userDto.getUserName());
    if (existingUser.isPresent()) {
      log.info("User with the same username already exists");
      return Optional.of(createDto(existingUser.get()));
    } else {
      log.info("New User saved with username: {}", newUser.getUserName());
      return Optional.of(createDto(userRepository.save(newUser)));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<UserDto> updateUser(Long id, UserDto userDto) {
    Optional<User> existingUser = userRepository.findById(id);
    if (existingUser.isPresent()) {
      User updateUser = deriveUserFromDto(userDto);
      if (updateUser == null) {
        log.error("UserDto is null");
        return Optional.empty();
      }
      if (existingUser.get().merge(updateUser)) {
        userRepository.save(existingUser.get());
      }
      return Optional.of(createDto(existingUser.get()));
    }
    return Optional.empty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserDto> listAllUsers() {
    List<UserDto> users = new ArrayList<>();
    for (User user : userRepository.findAll()) {
      users.add(createDto(user));
    }
    return users;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<UserDto> getUserInfo(Long id) {
    return userRepository.findById(id).map(user -> createDto(user));
  }

  /**
   * @param userDto UserDto
   * @return User created from Dto
   */
  private User deriveUserFromDto(UserDto userDto) {
    return User.builder()
        .userName(userDto.getUserName())
        .firstName(userDto.getFirstName())
        .lastName(userDto.getLastName())
        .build();
  }

  /**
   * @param user User
   * @return UserDto created from User
   */
  private UserDto createDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .userName(user.getUserName())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .build();
  }

}
