package com.example.demo.application.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit test class for {@link UserController} class.
 *
 * @author szigeti.attila96@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  private static final Long USER_ID = 1L;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @Test
  public void testGetUserSuccessfully() {
    when(userService.getUserInfo(USER_ID)).thenReturn(Optional.of(createUserDto("getUser")));
    assertEquals(HttpStatus.OK, userController.getUserInfo(USER_ID).getStatusCode());
  }

  @Test
  public void testGetUserNotFound() {
    when(userService.getUserInfo(USER_ID)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, userController.getUserInfo(USER_ID).getStatusCode());
  }

  @Test
  public void testListUsersSuccessfully() {
    when(userService.listAllUsers()).thenReturn(new ArrayList<>() {{
      add(createUserDto("firstUser"));
      add(createUserDto("secondUser"));
    }});

    ResponseEntity<List<UserDto>> resultList = userController.listAllUsers();
    assertEquals(HttpStatus.OK, resultList.getStatusCode());
    assertNotNull(resultList.getBody());
    assertEquals(2, resultList.getBody().size());
  }

  @Test
  public void testListUsersNotFound() {
    when(userService.listAllUsers()).thenReturn(new ArrayList<>());
    assertEquals(HttpStatus.NOT_FOUND, userController.listAllUsers().getStatusCode());
  }

  @Test
  public void testCreateUserSuccessfully() {
    UserDto userDto = createUserDto("createUser");
    when(userService.createUser(userDto)).thenReturn(Optional.of(userDto));
    assertEquals(HttpStatus.OK, userController.create(userDto).getStatusCode());
  }

  @Test
  public void testCreateUserNotFound() {
    UserDto userDto = createUserDto("createUser");
    when(userService.createUser(userDto)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, userController.create(userDto).getStatusCode());
  }

  @Test
  public void testUpdateUserSuccessfully() {
    UserDto userDto = createUserDto("updateUser");
    when(userService.updateUser(USER_ID, userDto)).thenReturn(Optional.of(userDto));
    assertEquals(HttpStatus.OK, userController.update(userDto, USER_ID).getStatusCode());
  }

  @Test
  public void testUpdateUserNotFound() {
    UserDto userDto = createUserDto("updateUser");
    when(userService.updateUser(USER_ID, userDto)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, userController.update(userDto,USER_ID).getStatusCode());
  }

  private UserDto createUserDto(String userName) {
    return UserDto.builder()
        .userName(userName)
        .firstName("firstName")
        .lastName("lastName")
        .build();
  }
}
