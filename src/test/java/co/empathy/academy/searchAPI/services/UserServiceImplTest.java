package co.empathy.academy.searchAPI.services;

import co.empathy.academy.searchAPI.exceptions.DuplicatedUserException;
import co.empathy.academy.searchAPI.exceptions.UserNotFoundException;
import co.empathy.academy.searchAPI.models.User;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl();
    }

    @Test
    public void testFindAllUsers() throws DuplicatedUserException {
        assertTrue(userService.findAllUsers().isEmpty());
        User user1 = new User(1, "John", "Doe");
        User user2 = new User(2, "Jane", "Doe");
        userService.saveUser(user1);
        userService.saveUser(user2);
        Collection<User> users = userService.findAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void testFindUserById() throws UserNotFoundException, DuplicatedUserException {
        User user = new User(1, "John", "Doe");
        userService.saveUser(user);
        assertEquals(user, userService.findUserById(1));
    }

    @Test
    public void testFindUserByIdThrowsException() {
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    public void testSaveUser() throws DuplicatedUserException, UserNotFoundException {
        User user = new User(1L, "John", "Doe");
        userService.saveUser(user);
        assertEquals(user, userService.findUserById(1L));
    }

    @Test
    public void testSaveUserThrowsException() throws DuplicatedUserException {
        User user = new User(1L, "John", "Doe");
        userService.saveUser(user);
        assertThrows(DuplicatedUserException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testUpdateUser() throws UserNotFoundException, DuplicatedUserException {
        User user = new User(1L, "John", "Doe");
        userService.saveUser(user);
        User updatedUser = new User(1L, "Jane", "Doe");
        userService.updateUser(1L, updatedUser);
        assertEquals(updatedUser, userService.findUserById(1L));
    }

    @Test
    public void testUpdateUserThrowsException() {
        User user = new User(1L, "John", "Doe");
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, user));
    }

    @Test
    public void testSaveAll() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn(Files.readAllBytes(Paths.get("src/test/java/co/empathy/academy/searchAPI/services/users.json")));
        userService.saveAll(mockFile);
        Collection<User> savedUsers = userService.findAllUsers();
        assertEquals(3, userService.findAllUsers().size());
    }

    @Test
    public void testSaveAllAsync() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn(Files.readAllBytes(Paths.get("src/test/java/co/empathy/academy/searchAPI/services/users.json")));
        userService.saveAllAsync(mockFile);
        CompletableFuture<String> task = userService.getTask("saveAllAsync");
        task.get(); // Esperar hasta que la tarea se complete
        assertEquals(3, userService.findAllUsers().size());
    }

}
