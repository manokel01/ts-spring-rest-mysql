package tinysensormanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tinysensormanager.dto.DeviceDTO;
import tinysensormanager.model.Device;
import tinysensormanager.model.User;
import tinysensormanager.repo.DeviceRepo;
import tinysensormanager.service.exceptions.EntityNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * This class implements the {@link IDeviceService} interface and provides the business logic for handling devices in the system.
 * It uses the {@link DeviceRepo} interface to interact with the database.
 */
@Service
public class DeviceServiceImpl implements IDeviceService {

    private final DeviceRepo deviceRepo;

    /**
     * Constructor to inject the {@link DeviceRepo} instance via Spring's dependency injection.
     * @param deviceRepo The repository for interacting with Device entities in the database.
     */
    @Autowired
    public DeviceServiceImpl(DeviceRepo deviceRepo) {
        this.deviceRepo = deviceRepo;
    }

   /**
    * Adds a new {@link Device} entity to the database by saving the {@link Device} object
    * converted from the provided {@link DeviceDTO} object.
    * @param deviceDTO The {@link DeviceDTO} object representing the new device to be added.
    * @return The {@link Device} object that has been added to the database.
    */
    @Transactional
    @Override
    public Device addDevice(DeviceDTO deviceDTO) {
        return deviceRepo.save(convertToDevice(deviceDTO));
    }

    /**
     * Retrieves a list of all {@link Device} entities in the database.
     * @return A list of all {@link Device} entities in the database.
     */
    @Override
    public List<Device> findAllDevices() {
        return deviceRepo.findAll();
    }

    /**
     * Retrieves a {@link Device} entity from the database by its ID.
     * @param id The ID of the {@link Device} entity to retrieve.
     * @return The {@link Device} entity corresponding to the provided ID.
     * @throws EntityNotFoundException if the {@link Device} entity with the provided ID is not found in the database.
     */
    @Override
    public Device findDeviceById(Long id) throws EntityNotFoundException {
        Optional<Device> device = deviceRepo.findById(id);
        if (device.isEmpty()) throw new EntityNotFoundException(Device.class, id);
        return device.get();
    }

    /**
     * Updates a Device entity in the database by saving the {@link Device} object converted from
     * the provided {@link DeviceDTO} object.
     * @param deviceDTO The {@link DeviceDTO} representing the {@link DeviceDTO} entity to be updated.
     * @return The {@link DeviceDTO} object that has been updated in the database.
     * @throws EntityNotFoundException if the {@link DeviceDTO} entity with the provided ID is not found in the database.
     */
    @Transactional
    @Override
    public Device updateDevice(DeviceDTO deviceDTO) throws EntityNotFoundException {
        Device device = deviceRepo.findDeviceById(deviceDTO.getId());
        if (device == null) throw new EntityNotFoundException(Device.class, deviceDTO.getId());
        return deviceRepo.save(convertToDevice(deviceDTO));
    }

    /**
     * Deletes a {@link Device} entity from the database by its ID.
     * @param id The ID of the {@link Device} entity to delete.
     * @throws EntityNotFoundException if the {@link Device} entity with the provided ID is not found in the database.
     */
    @Transactional
    @Override
    public void deleteDevice(Long id) throws EntityNotFoundException {
        try {
            deviceRepo.deleteById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException(Device.class, id);
        }
    }

    /**
     * Retrieves a list of {@link Device} entities from the database whose model names start with the provided string.
     * @param model The string representing the beginning of the model names to search for.
     * @return A list of {@link Device} entities whose model names start with the provided string.
     * @throws EntityNotFoundException if no {@link Device entities are found in the database with a model name starting with the provided string.
     */
    @Override
    public List<Device> findDeviceByModel(String model) throws EntityNotFoundException {
        List<Device> devices;
        devices = deviceRepo.findByModelStartingWith(model);
        if (devices.size() == 0) throw new EntityNotFoundException(User.class, 0L);
        return devices;
    }

    /**
     * Maps a {@link DeviceDTO} object to a Device object.
     * @param dto
     * @return
     */
    private static Device convertToDevice(DeviceDTO dto) {
        return new Device(dto.getId(),
                dto.getModel(),
                dto.getSerialnumber(),
                dto.getMac(),
                dto.getIp(),
                dto.getImageUrl()
        );
    }
}
