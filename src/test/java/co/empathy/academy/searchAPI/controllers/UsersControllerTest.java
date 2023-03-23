package co.empathy.academy.searchAPI.controllers;

import co.empathy.academy.searchAPI.exceptions.UserNotFoundException;
import co.empathy.academy.searchAPI.models.User;
import co.empathy.academy.searchAPI.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;


import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UsersControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    UsersController usersController;

    @Test
    public void givenUsers_whenFindAllUsers_thenStatus200AndJsonArray() throws Exception {
        User user1 = new User(1L, "John Doe", "john.doe@example.com");
        User user2 = new User(2, "Grace Holland", "grace.doe@example.com");
        Collection<User> users = Arrays.asList(user1, user2);
        when(userService.findAllUsers()).thenReturn(users);
        ResponseEntity<Collection<User>> response = usersController.findAllUsers();
        assertEquals(users, response.getBody());
    }

    @Test
    public void givenId_whenFindUserById_thenStatus200AndJson() throws Exception {
        User user = new User(1L, "John Doe", "john.doe@example.com");
        when(userService.findUserById(1L)).thenReturn(user);
        ResponseEntity<User> response = usersController.findUserById(1L);
        assertEquals(user, response.getBody());
    }

    @Test
    public void givenId_whenFindUserById_thenStatus404() throws Exception {
        when(userService.findUserById(1L)).thenThrow(new UserNotFoundException(1L));
        assertThrows(UserNotFoundException.class, () -> usersController.findUserById(1L));
    }

    @Test
    public void givenUser_whenSaveUser_thenStatus201() throws Exception {
        User user = new User(1L, "John Doe", "john.doe@example.com");
        when(userService.saveUser(user)).thenReturn(user);
        ResponseEntity<User> response = usersController.saveUser(user);
        assertEquals(user, response.getBody());
    }

    @Test
    public void givenUser_whenUpdateUser_thenStatus200() throws Exception {
        User user = new User(1L, "John Doe", "john.doe@example.com");
        when(userService.updateUser(1L, user)).thenReturn(user);
        ResponseEntity<User> response = usersController.updateUser(1L, user);
        assertEquals(user, response.getBody());
    }

    @Test
    public void givenId_whenDeleteUser_thenStatus204() throws Exception {

    }
}
