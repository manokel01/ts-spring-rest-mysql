package tinysensormanager.service.exceptions;

/**
 *  Exception to be thrown when an entity with a given id cannot be found in the database.
 *
 * @author manokel01
 * @version 1.0
 */
public class EntityNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(Class<?> entityClass, Long id) {
        super("Entity" + entityClass.getSimpleName() + " with id" + id + " does not exist");
    }
}
