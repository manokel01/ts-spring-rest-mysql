package tinysensormanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tinysensormanager.model.Device;

import java.util.List;

/**
 * This interface is used to access the database {@link Device} table.
 * It is used by Spring Data JPA to generate the implementation.
 * @author manokel01
 * @version 1.0.0
 */
@Repository
public interface  DeviceRepo extends JpaRepository<Device, Long> {
    /**
     * This method is used to find a {@link Device} by model.
     * @param model The model of the {@link Device} to find.
     * @return A {@link List} of {@link Device} objects.
     */
    List<Device> findByModelStartingWith(String model);

    /**
     * This method is used to find a {@link Device} by id.
     * @param id The id of the {@link Device} to find.
     * @return A {@link Device} object.
     */
    Device findDeviceById(Long id);
}
