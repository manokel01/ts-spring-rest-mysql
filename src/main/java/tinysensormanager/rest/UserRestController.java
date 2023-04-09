package tinysensormanager.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tinysensormanager.dto.UserDTO;
import tinysensormanager.model.User;
import tinysensormanager.service.IUserService;
import tinysensormanager.service.exceptions.EntityNotFoundException;
import tinysensormanager.service.util.LoggerUtil;
import tinysensormanager.validator.UserValidator;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *  This REST controller allows users {@link User} to perform CRUD operations on User entities via HTTP endpoints.
 * This controller uses the Spring Framework for dependency injection and validation.
 * Swagger is used for API documentation.
 *
 * @author manokel01
 * @version 1.0
 * @Date 2023-04-07
 */


@RestController
@RequestMapping("/api") public class UserRestController {
    private final IUserService userService;
    private final UserValidator userValidator;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    /**
     * Constructor for UserRestController, which is used for dependency injection by Spring.
     *
     * @param userService   A service object for managing {@link User} entities.
     * @param userValidator A validator object for validating {@link UserDTO} objects before they are persisted.
     * @param messageSource A message source object for getting localized error messages.
     */
    @Autowired
    public UserRestController(IUserService userService, UserValidator userValidator,
                              MessageSource messageSource) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.messageSource = messageSource;
    }

    /**
     * A method that is run after this object is instantiated, which initializes the MessageSourceAccessor object.
     */
    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    /**
     * Gets users {@link User} with a given lastname, or users whose name start with a given string.
     *
     * @param lastname The last name or starting string of the name of the users {@link User} to be retrieved.
     *
     * @return ResponseEntity<List <UserDTO>> A list of UserDTO objects representing the retrieved users.
     */
    @Operation(summary = "Get users by their lastname or starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid lastname supplied",
                    content = @Content)})
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getUsersByLastname(@RequestParam("lastname") String lastname) {
        List<User> users;
        try {
            users = userService.findUserByLastname(lastname);
            List<UserDTO> usersDTO = new ArrayList<>();
            for (User user : users) {
                usersDTO.add(new UserDTO(user.getId(),
                                user.getFirstname(),
                                user.getLastname(),
                                user.getEmail(),
                                user.getAddress(),
                                user.getImageUrl()
                        )
                );
            }
            return new ResponseEntity<>(usersDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets a single {@link User} by ID.
     *
     * @param userId The ID of the {@link User} to retrieve.
     *
     * @return ResponseEntity<UserDTO> A {@link UserDTO} object representing the retrieved {@link User} entity.
     */
    @Operation(summary = "Get a User by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {
        User user;
        try {
            user = userService.findUserById(userId);
            UserDTO userDTO = new UserDTO(user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getAddress(),
                    user.getImageUrl()
            );
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all Users {@link User}.
     *
     * @return ResponseEntity<List <User>> A list of User objects representing all {@link User} in the system.
     */
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) })})
    @RequestMapping(value = "/users/all", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Adds a new {@link User} to the system.
     *
     * @param dto A {@link UserDTO} object representing the new {@link User} entity to be added.
     * @param bindingResult A BindingResult object that contains object validation errors, if any.
     *
     * @return ResponseEntity<UserDTO> A {@link UserDTO} object representing the new {@link User} entity that was just created.
     */
    @Operation(summary = "Add a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO dto,
                                           BindingResult bindingResult) {
        userValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.addUser(dto);
        UserDTO userDTO = map(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(userDTO);
    }

    /**
     * Deletes a {@link User} with a given ID from the system.
     *
     * @param userId The ID of the {@link User} to be deleted.
     *
     * @return ResponseEntity<UserDTO> A {@link UserDTO} object representing the {@link User} entity that was just deleted.
     */
    @Operation(summary = "Delete a User by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("userId") Long userId) {
        try {
            User user = userService.findUserById(userId);
            userService.deleteUser(userId);
            UserDTO userDTO = map(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates an existing {@link User} in the system.
     *
     * @param userId The ID of the {@link User} entity to be updated.
     * @param dto A {@link UserDTO} object representing the updated User entity.
     * @param bindingResult A BindingResult object that contains object validation errors, if any.
     *
     * @return ResponseEntity<UserDTO> A {@link UserDTO} object representing the updated {@link User} entity.
     */
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Long userId,
                                              @RequestBody UserDTO dto, BindingResult bindingResult) {
        userValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            dto.setId(userId);
            User user = userService.updateUser(dto);
            UserDTO userDTO = map(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Maps a User entity to a {@link UserDTO} entity.
     *
     * @param user The {@link User} entity to be mapped.
     *
     * @return {@link UserDTO} A {@link UserDTO} object representing the mapped {@link User} entity.
     */
    private UserDTO map(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setImageUrl(user.getImageUrl());
        return userDTO;
    }
}