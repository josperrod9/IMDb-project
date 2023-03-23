package co.empathy.academy.searchAPI.controllers;

import co.empathy.academy.searchAPI.exceptions.DuplicatedUserException;
import co.empathy.academy.searchAPI.exceptions.UserNotFoundException;
import co.empathy.academy.searchAPI.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;


@Tag(name = "users", description = "the users API")
public interface UsersApi {

    @GetMapping
    @Operation(operationId = "findAllUsers", summary = "Find all users", tags = { "users" },
            responses = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))) })
    ResponseEntity<Collection<User>> findAllUsers();

    @GetMapping("/{id}")
    @Operation(operationId = "findUserById", summary = "Coitooooooo", tags = { "users" },
            parameters = { @Parameter(in = ParameterIn.PATH, name = "id", description = "User Id", example = "1")},
            responses = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid User ID supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    ResponseEntity<User> findUserById(@PathVariable(value = "id") long id) throws UserNotFoundException;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createUser", summary = "Create a new user", tags = { "users" },
            responses = { @ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid User supplied", content = @Content),
                    @ApiResponse(responseCode = "409", description = "User already exists", content = @Content) })
    ResponseEntity<User> saveUser(@Validated @RequestBody User user) throws DuplicatedUserException;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "deleteUser", summary = "Delete a user by ID", tags = { "users" },
            parameters = { @Parameter(in = ParameterIn.PATH, name = "id", description = "User Id")},
            responses = { @ApiResponse(responseCode = "204", description = "User deleted", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid User ID supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    void deleteUser(@PathVariable(value = "id") long id) throws UserNotFoundException;

    @PutMapping("/{id}")
    @Operation(operationId = "updateUser", summary = "Update a user by ID", tags = { "users" },
            parameters = { @Parameter(in = ParameterIn.PATH, name = "id", description = "User Id")},
            responses = { @ApiResponse(responseCode = "200", description = "User updated", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid User ID supplied", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
    ResponseEntity<User> updateUser(@PathVariable(value = "id") long id, @Validated @RequestBody User user) throws UserNotFoundException ;

    @PostMapping("/upload")
    @Operation(operationId = "uploadUsers", summary = "Upload a list of new users", tags = { "users" },
            responses = { @ApiResponse(responseCode = "200", description = "Users saved", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid users supplied", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Some user already exists", content = @Content) })
    ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file);

    @PostMapping("/upload-async")
    @Operation(operationId = "uploadUsers-async", summary = "Upload a list of new users", tags = { "users" },
            responses = { @ApiResponse(responseCode = "202", description = "Users are being processed", content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid users supplied", content = @Content)})
    ResponseEntity<HttpStatus> uploadUsersAsync(@RequestParam("file") MultipartFile file) throws IOException;

    @GetMapping("/upload-async/status")
    @Operation(operationId = "uploadUsers-async-status", summary = "Check upload-async operation status", tags = { "users" },
            responses = { @ApiResponse(responseCode = "200", description = "Operation has been completed", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "202", description = "Operation still in progress", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Operation has been not found", content = @Content)})
    ResponseEntity<String> uploadUsersAsyncStatus(@RequestParam(value = "taskId") String taskId) throws InterruptedException, ExecutionException;
}
