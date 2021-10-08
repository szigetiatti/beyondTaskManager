package com.example.demo.application.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Unit test class for {@link UserService} interface.
 *
 * @author szigeti.attila96@gmail.com
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  private static final Long USER_ID = 1L;

  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private UserServiceImpl userService;

  @Test
  public void testCreateUserUserDtoIsNull() {
    Assert.assertTrue(userService.createUser(null).isEmpty());
  }

  @Test
  public void testCreateUserUserAlreadyExists() {
    UserDto userDto = createUserDto("existingUser");
    User user = createUser("existingUser");

    when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));

    Optional<UserDto> createdUserDtoOpt = userService.createUser(userDto);
    assertFalse(createdUserDtoOpt.isEmpty());
    assertEquals(createdUserDtoOpt.get().getUserName(), userDto.getUserName());
    assertEquals(user.getFirstName(), createdUserDtoOpt.get().getFirstName());
    assertEquals(user.getLastName(), createdUserDtoOpt.get().getLastName());
  }

  @Test
  public void testCreateUserSuccessfully() {
    UserDto userDto = createUserDto("existingUser");
    User user = createUser("existingUser");

    when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenAnswer((i -> i.getArguments()[0]));

    Optional<UserDto> createdUserDtoOpt = userService.createUser(userDto);
    assertFalse(createdUserDtoOpt.isEmpty());
    assertEquals(createdUserDtoOpt.get().getUserName(), userDto.getUserName());
    assertEquals(createdUserDtoOpt.get().getFirstName(), userDto.getFirstName());
    assertEquals(createdUserDtoOpt.get().getLastName(), userDto.getLastName());
  }

  @Test
  public void testUpdateUserCannotBeFound() {
    Assert.assertTrue(userService.updateUser(USER_ID, null).isEmpty());
    Assert.assertTrue(userService.updateUser(null, null).isEmpty());
  }

  @Test(expected = NullPointerException.class)
  public void testUpdateUserUserDtoIsNull() {
    User user = createUser("userDtoIsNull");
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    Assert.assertTrue(userService.updateUser(USER_ID, null).isEmpty());
  }

  @Test
  public void testUpdateUserSuccessfully() {
    User user = createUser("updateUser");
    UserDto userDto = createUserDto("updatedUser");
    ReflectionTestUtils.setField(userDto, "firstName", "merged");
    ReflectionTestUtils.setField(userDto, "lastName", "merged");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    Optional<UserDto> result = userService.updateUser(USER_ID, userDto);
    assertTrue(result.isPresent());
    assertEquals(userDto.getFirstName(), user.getFirstName());
    assertEquals(userDto.getLastName(), user.getLastName());
    assertNotEquals(userDto.getUserName(), user.getUserName());
  }

  @Test
  public void testGetUserCannotBeFound() {
    Assert.assertTrue(userService.getUserInfo(null).isEmpty());
    Assert.assertTrue(userService.getUserInfo(USER_ID).isEmpty());
  }

  @Test
  public void testGetUserSuccessfully() {
    User user = createUser("getUser");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    Optional<UserDto> result = userService.getUserInfo(USER_ID);
    assertTrue(result.isPresent());
    assertEquals(user.getUserName(), result.get().getUserName());
    assertEquals(user.getFirstName(), result.get().getFirstName());
    assertEquals(user.getLastName(), result.get().getLastName());
  }

  @Test
  public void testListUsersCannotBeFound() {
    Assert.assertTrue(userService.listAllUsers().isEmpty());
  }

  @Test
  public void testListUsersSuccessfully() {
    UserDto userDto = createUserDto("listUser");
    List<User> userList = new ArrayList<>() {{
      add(createUser("listUser1"));
      add(createUser("listUser2"));
    }};

    when(userRepository.findAll()).thenReturn(userList);

    List<UserDto> result = userService.listAllUsers();
    assertFalse(result.isEmpty());
    assertEquals(userList.size(), result.size());
  }

  private UserDto createUserDto(String userName) {
    return UserDto.builder()
        .userName(userName)
        .firstName("dtoFirstName")
        .lastName("dtoLastName")
        .build();
  }

  private User createUser(String userName) {
    return User.builder()
        .userName(userName)
        .firstName("firstName")
        .lastName("lastName")
        .build();
  }
}
