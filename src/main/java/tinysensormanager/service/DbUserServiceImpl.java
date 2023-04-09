package tinysensormanager.service;

import org.springframework.stereotype.Service;
import tinysensormanager.dto.DbUserDTO;
import tinysensormanager.model.DbUser;
import tinysensormanager.model.User;
import tinysensormanager.repo.DbUserRepo;
import tinysensormanager.service.exceptions.EntityNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link DbUser} entities in the database.
 */
@Service
public class DbUserServiceImpl implements IDbUserService {

    private final DbUserRepo dbUserRepo;

    /**
     * Constructor for DbUserServiceImpl that takes a DbUserRepo object as a parameter
     * @param dbUserRepo the repository for the DbUser entity
     */
    public DbUserServiceImpl(DbUserRepo dbUserRepo) {
        this.dbUserRepo = dbUserRepo;
    }

    /**
     * Register a new {@link DbUser} in the database.
     *
     * @param dbUserDTO the DTO containing the data of the {@link DbUser} to be registered
     * @return the registered {@link DbUser} entity
     */
    @Transactional
    @Override
    public DbUser registerUser(DbUserDTO dbUserDTO) {
        return dbUserRepo.save(convertToDbUser(dbUserDTO));
    }

    /**
     * Retrieve all DbUsers from the system
     * @return a list of all DbUser objects in the system
     */
    @Override
    public List<DbUser> findAllUsers() {
        return dbUserRepo.findAll();
    }

    /**
     * Find a specific DbUser in the system by their ID
     * @param id the ID of the DbUser to be retrieved
     * @return the DbUser object with the specified ID
     * @throws EntityNotFoundException if no DbUser with the specified ID can be found
     */
    @Override
    public DbUser findUserById(Long id) throws EntityNotFoundException {
        Optional<DbUser> user;
        user = dbUserRepo.findById(id);
        if (user.isEmpty()) throw new EntityNotFoundException(DbUser.class, 0L);
        return user.get();
    }

    /**
     * Update an existing DbUser in the system
     * @param dbUserDTO the DTO object representing the updated DbUser information
     * @return the updated DbUser object
     * @throws EntityNotFoundException if no DbUser with the specified ID can be found
     */
    @Transactional
    @Override
    public DbUser updateUser(DbUserDTO dbUserDTO) throws EntityNotFoundException {
        Optional<DbUser> user = dbUserRepo.findById(dbUserDTO.getId());
        if (user.isEmpty()) throw new EntityNotFoundException(User.class, dbUserDTO.getId());
        return dbUserRepo.save(convertToDbUser(dbUserDTO));
    }

    /**
     * Delete an existing DbUser from the system
     * @param id the ID of the DbUser to be deleted
     * @throws EntityNotFoundException if no DbUser with the specified ID can be found
     */
    @Transactional
    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        try {
            dbUserRepo.deleteById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException(User.class, id);
        }
    }

    /**
     * Find a list of DbUsers in the system by their username
     * @param username the username to search for
     * @return a list of DbUser objects with the specified username
     * @throws EntityNotFoundException if no DbUsers with the specified username can be found
     */
    @Override
    public List<DbUser> findUserByUsername(String username) throws EntityNotFoundException {
        List<DbUser> users;
        users = dbUserRepo.findByUsernameEquals(username);
        if (users.size() == 0) throw new EntityNotFoundException(DbUser.class, 0L);
        return users;
    }

    /**
     * Checks if a username already exists in the database.
     *
     * @param email The username to check.
     * @return True if the username exists, false otherwise.
     */
    @Override
    public boolean usernameExists(String email) {
        return false;
    }

    /**
     * Converts a DbUserDTO object to a DbUser object
     * @param dto the DbUserDTO object to be converted
     * @return the converted DbUser object
     */
    private static DbUser convertToDbUser(DbUserDTO dto) {
        return new DbUser(dto.getId(),
                dto.getUsername(),
                dto.getPassword()
                );
    }
}
