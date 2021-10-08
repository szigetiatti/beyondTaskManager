package com.example.demo.application.user;

import java.util.List;
import java.util.Optional;

/**
 * Defines basic functions for User entity.
 *
 * @author szigeti.attila96@gmail.com
 */
public interface UserService {

  /**
   * Create a User from Dto,
   * gives back the Dto representation for it.
   *
   * @param user UserDto
   * @return UserDto wrapped into Optional.
   */
  Optional<UserDto> createUser(UserDto user);

  /**
   * Updates a User matching with id,
   * gives back the updated User Dto representation.
   *
   * @param id   Long
   * @param user UserDto
   * @return UserDto wrapped into Optional.
   */
  Optional<UserDto> updateUser(Long id, UserDto user);

  /**
   * @return all UserDtos for a user in a List.
   */
  List<UserDto> listAllUsers();

  /**
   * @param id Long
   * @return UserDto wrapped into Optional.
   */
  Optional<UserDto> getUserInfo(Long id);

}
