package co.empathy.academy.searchAPI.controllers;

import co.empathy.academy.searchAPI.exceptions.DuplicatedUserException;
import co.empathy.academy.searchAPI.exceptions.UserNotFoundException;
import co.empathy.academy.searchAPI.models.User;
import co.empathy.academy.searchAPI.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/users")
public class UsersController implements UsersApi{

    private final UserService userService;

    @Autowired
    public UsersController(UserService usersService) {
        this.userService = usersService;
    }

    @Override
    public ResponseEntity<Collection<User>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Override
    public ResponseEntity<User> findUserById(@PathVariable(value = "id") long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Override
    public ResponseEntity<User> saveUser(@Validated @RequestBody User user) throws DuplicatedUserException {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @Override
    public void deleteUser(@PathVariable(value = "id") long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

    @Override
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") long id, @Validated @RequestBody User user) throws UserNotFoundException {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Override
    public ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file){
        try {
            userService.saveAll(file);
            return ResponseEntity.ok("Users saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving users");
        }
    }

    @Override
    public ResponseEntity<HttpStatus> uploadUsersAsync(@RequestParam("file") MultipartFile file) throws IOException {
        userService.saveAllAsync(file);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> uploadUsersAsyncStatus(@RequestParam(value = "taskId") String taskId) throws InterruptedException, ExecutionException {
        CompletableFuture<String> task = userService.getTask(taskId);
        if (task == null) {
            return new ResponseEntity<>("The task with Id " + taskId + " didn't exist", HttpStatus.NOT_FOUND);
        } else if (task.isDone()) {
            return new ResponseEntity<>(task.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The task with Id " + taskId + " is in progress", HttpStatus.ACCEPTED);
        }
    }
}
