package co.empathy.academy.searchAPI.services;

import co.empathy.academy.searchAPI.exceptions.DuplicatedUserException;
import co.empathy.academy.searchAPI.exceptions.UserNotFoundException;
import co.empathy.academy.searchAPI.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Service
public interface UserService {
    Collection<User> findAllUsers();

    User findUserById(long id) throws UserNotFoundException;

    User saveUser(User user) throws DuplicatedUserException;

    void deleteUser(long id) throws UserNotFoundException;

    User updateUser(long id, User user) throws UserNotFoundException;

    void saveAll(MultipartFile file) throws IOException;

    void saveAllAsync(MultipartFile file) throws IOException;

    CompletableFuture<String> getTask(String taskId);

}
