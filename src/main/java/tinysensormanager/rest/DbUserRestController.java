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
import tinysensormanager.dto.DbUserDTO;
import tinysensormanager.dto.UserDTO;
import tinysensormanager.model.DbUser;
import tinysensormanager.service.IDbUserService;
import tinysensormanager.service.exceptions.EntityNotFoundException;
import tinysensormanager.service.util.LoggerUtil;
import tinysensormanager.validator.DbUserValidator;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *  This REST controller allows Database users {@link DbUser} to perform CRUD operations
 * on {@link DbUser} entities via HTTP endpoints.
 * This controller uses the Spring Framework for dependency injection and validation.
 * Swagger is used for API documentation.
 *
 * @author manokel01
 * @version 1.0
 * @Date 2023-04-07
 */
@RestController
@RequestMapping("/api")
public class DbUserRestController {
    private final IDbUserService dbUserService;
    private final DbUserValidator dbUserValidator;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    /**
     * Constructor for the DbUserRestController
     * @param dbUserService the service layer for the DbUserRestController
     * @param dbUserValidator the validator for the DbUserRestController
     * @param messageSource the message source for the DbUserRestController
     */
    @Autowired
    public DbUserRestController(IDbUserService dbUserService, DbUserValidator dbUserValidator,
                              MessageSource messageSource) {
        this.dbUserService = dbUserService;
        this.dbUserValidator = dbUserValidator;
        this.messageSource = messageSource;
    }

    /**
     * This method is used to initialize the MessageSourceAccessor
     */
    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    /**
     * This is a GET endpoint that returns a list of all {@link DbUser} database users.
     * @return a list of all database users
     */
    @Operation(summary = "Get database users by their lastname or starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database Users Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid username supplied",
                    content = @Content)})
    @RequestMapping(path = "/dbusers", method = RequestMethod.GET)
    public ResponseEntity<List<DbUserDTO>> getUsersByLastname(@RequestParam("lastname") String username) {
        List<DbUser> dbUsers;
        try {
            dbUsers = dbUserService.findUserByUsername(username);
            List<DbUserDTO> dbUsersDTO = new ArrayList<>();
            for (DbUser dbUser : dbUsers) {
                dbUsersDTO.add(new DbUserDTO(dbUser.getId(),
                        dbUser.getUsername(),
                        dbUser.getPassword()
                        )
                );
            }
            return new ResponseEntity<>(dbUsersDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * This is a GET endpoint that returns a database user {@link DbUser} by id.
     * @param dbUserId the id of the database user
     * @return the database user
     */
    @Operation(summary = "Get a Database User by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database User Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DbUserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Database User not found",
                    content = @Content)})
    @RequestMapping(value = "/dbusers/{dbuserId}", method = RequestMethod.GET)
    public ResponseEntity<DbUserDTO> getUser(@PathVariable("dbuserId") Long dbUserId) {
        DbUser dbUser;
        try {
            dbUser = dbUserService.findUserById(dbUserId);
            DbUserDTO dbUserDTO = new DbUserDTO(dbUser.getId(),
                    dbUser.getUsername(),
                    dbUser.getPassword()
            );
            return new ResponseEntity<>(dbUserDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This is a GET endpoint that returns a list of all {@link DbUser} instances.
     * @return a list of all database users
     */
    @Operation(summary = "Get all Database Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all database users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DbUserDTO.class)) })})
    @RequestMapping(value = "/dbusers/all", method = RequestMethod.GET)
    public ResponseEntity<List<DbUser>> getAllUsers() {
        List<DbUser> dbUsers = dbUserService.findAllUsers();
        return new ResponseEntity<>(dbUsers, HttpStatus.OK);
    }

    /**
     * This is a POST endpoint that creates a {@link DbUser}.
     * @param dto the database user to be created
     * @param bindingResult the binding result
     * @return the created {@link DbUser}
     */
    @Operation(summary = "Add a Database User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "DatabaseUser created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DbUserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/dbusers", method = RequestMethod.POST)
    public ResponseEntity<DbUserDTO> addUser(@RequestBody DbUserDTO dto,
                                           BindingResult bindingResult) {
        dbUserValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DbUser dbUser = dbUserService.registerUser(dto);
        DbUserDTO dbUserDTO = map(dbUser);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dbUserDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(dbUserDTO);
    }

    /**
     * This is a DELETE endpoint that deletes a {@link DbUser} by id.
     * @param dbUserId
     * @return
     */
    @Operation(summary = "Delete a Database User by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database User Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DbUserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Database User not found",
                    content = @Content)})
    @RequestMapping(value = "/dbusers/{dbuserId}", method = RequestMethod.DELETE)
    public ResponseEntity<DbUserDTO> deleteUser(@PathVariable("dbuserId") Long dbUserId) {
        try {
            DbUser dbUser = dbUserService.findUserById(dbUserId);
            dbUserService.deleteUser(dbUserId);
            DbUserDTO dbUserDTO = map(dbUser);
            return new ResponseEntity<>(dbUserDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This is a PUT endpoint that updates a {@link DbUser}.
     * @param dbUserId the id of the {@link DbUser} to be updated
     * @param dto the database user to be updated
     * @param bindingResult the binding result
     * @return the updated {@link DbUser}
     */
    @Operation(summary = "Database Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database User updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DbUserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Database User not found",
                    content = @Content) })
    @RequestMapping(value = "/dbusers/{dbuserId}", method = RequestMethod.PUT)
    public ResponseEntity<DbUserDTO> updateUser(@PathVariable("dbuserId") Long dbUserId,
                                              @RequestBody DbUserDTO dto, BindingResult bindingResult) {
        dbUserValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            dto.setId(dbUserId);
            DbUser dbUser = dbUserService.updateUser(dto);
            DbUserDTO dbUserDTO = map(dbUser);
            return new ResponseEntity<>(dbUserDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This is a helper method that maps a {@link DbUser} to a {@link DbUserDTO}.
     * @param dbUser the DbUser to be mapped
     * @return the mapped DbUserDTO
     */
    private DbUserDTO map(DbUser dbUser) {
        DbUserDTO dbUserDTO = new DbUserDTO();
        dbUserDTO.setId(dbUser.getId());
        dbUserDTO.setUsername(dbUser.getUsername());
        dbUserDTO.setPassword(dbUser.getPassword());
        return dbUserDTO;
    }
}
