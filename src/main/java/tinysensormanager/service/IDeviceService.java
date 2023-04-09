package tinysensormanager.service;

import tinysensormanager.dto.DeviceDTO;
import tinysensormanager.model.Device;
import tinysensormanager.service.exceptions.EntityNotFoundException;

import java.util.List;

/**
 * This interface defines the methods that a device service should implement.
 *
 * <p>The methods in this interface allow for the manipulation of {@link Device} data.</p>
 *
 * @author manokel01
 * @version 1.0
 */
public interface IDeviceService {

    /**
     * Adds a new device to the system.
     *
     * @param deviceDTO the {@link DeviceDTO} containing the device's information
     * @return the newly created {@link Device}
     */
    Device addDevice(DeviceDTO deviceDTO);

    /**
     * Retrieves a list of all devices in the system.
     *
     * @return a list of all {@link Device}s
     */
    List<Device> findAllDevices();

    /**
     * Retrieves a device by its ID.
     *
     * @param id the ID of the device to retrieve
     * @return the {@link Device} with the specified ID
     * @throws EntityNotFoundException if the {@link Device} with the specified ID is not found
     */
    Device findDeviceById(Long id) throws EntityNotFoundException;

    /**
     * Updates an existing device in the system.
     *
     * @param deviceDTO the {@link DeviceDTO} containing the updated device information
     * @return the updated {@link Device}
     * @throws EntityNotFoundException if the {@link Device} with the specified ID is not found
     */
    Device updateDevice(DeviceDTO deviceDTO) throws EntityNotFoundException;

    /**
     * Deletes a device from the system.
     *
     * @param id the ID of the device to delete
     * @throws EntityNotFoundException if the {@link Device} with the specified ID is not found
     */
    void deleteDevice(Long id) throws EntityNotFoundException;

    /**
     * Retrieves a list of devices with the specified model.
     *
     * @param model the model to search for
     * @return a list of {@link Device}s with the specified model
     * @throws EntityNotFoundException if no {@link Device}s with the specified model are found
     */
    List<Device> findDeviceByModel(String model) throws EntityNotFoundException;
}

