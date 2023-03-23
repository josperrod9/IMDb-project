package co.empathy.academy.searchAPI.services;

import co.empathy.academy.searchAPI.exceptions.DuplicatedUserException;
import co.empathy.academy.searchAPI.exceptions.UserNotFoundException;
import co.empathy.academy.searchAPI.models.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableAsync
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<String>> taskMap = new ConcurrentHashMap<>();

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User findUserById(long id) throws UserNotFoundException {
        if (this.users.containsKey(id)) {
            return this.users.get(id);
        }
        else{
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public User saveUser(User user) throws DuplicatedUserException {
        if (this.users.containsKey(user.getId())) {
            throw new DuplicatedUserException(user.getId());
        }
        this.users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long id,User user) throws UserNotFoundException {
        user.setId(id);
        if (this.users.containsKey(id)) {
            this.users.replace(id, user);
            return user;
        }
        else{
            throw new UserNotFoundException(user.getId());
        }
    }

    @Override
    public void saveAll(MultipartFile file) throws IOException {
        List<User> usersList = new ObjectMapper().readValue(file.getBytes(), new TypeReference<List<User>>() {});
        usersList.forEach(user -> this.users.put(user.getId(), user));
    }


    @Async
    @Override
    public void saveAllAsync(MultipartFile file) throws IOException {
        CompletableFuture<String> task = CompletableFuture.supplyAsync(() -> {
            try {
                List<User> usersList = new ObjectMapper().readValue(file.getBytes(), new TypeReference<List<User>>() {});
                usersList.forEach(user -> this.users.put(user.getId(), user));
                Thread.sleep(5000);
                return "Users saved";
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Error processing data", e);
            }
        });
        taskMap.put("saveAllAsync", task);
    }

    @Override
    public CompletableFuture<String> getTask(String taskId) {
        return taskMap.get(taskId);
    }

    public void deleteUser(long id) throws UserNotFoundException {
        if (this.users.containsKey(id)) {
            this.users.remove(id);
        }
        else{
            throw new UserNotFoundException(id);
        }
    }

}
