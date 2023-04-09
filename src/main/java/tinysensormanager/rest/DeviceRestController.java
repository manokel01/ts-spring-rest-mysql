package tinysensormanager.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tinysensormanager.dto.DeviceDTO;
import tinysensormanager.model.Device;
import tinysensormanager.service.IDeviceService;
import tinysensormanager.service.exceptions.EntityNotFoundException;
import tinysensormanager.service.util.LoggerUtil;
import tinysensormanager.validator.DeviceValidator;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 *  Rest controller for handling {@link Device}-related HTTP requests.
 *  This controller handles the retrieval, addition, modification and
 *  deletion of {@link Device} Devices.
 *
 *  @author manokel01
 *  @version 1.0.0
 *  @since 7/4/2023
 */


 @RestController
@RequestMapping("/api")
public class DeviceRestController {

    private final IDeviceService deviceService;
    private final DeviceValidator deviceValidator;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    /**
     * Constructor for DeviceRestController class, injects dependencies.
     * @param deviceService an instance of the device service.
     * @param deviceValidator a validator instance for checking device inputs.
     * @param messageSource an instance of a message source for localization of error messages.
     */
    public DeviceRestController(IDeviceService deviceService, DeviceValidator deviceValidator,
                                MessageSource messageSource) {
        this.deviceService = deviceService;
        this.deviceValidator = deviceValidator;
        this.messageSource = messageSource;
    }

    /**
     * This method is called after construction is completed, to initialize dependencies.
     */
    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    /**
     * Endpoint for GET requests to find {@link Device} by model name.
     * @param model parameter for filtering {@link Device} by model name or initials.
     * @return a Http response containing a list of devices by matching the provided model filter.
     */
    @Operation(summary = "Get devices by their model name or starting with initials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeviceDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid model name supplied",
                    content = @Content)})
    @RequestMapping(path = "/devices", method = RequestMethod.GET)
    public ResponseEntity<List<DeviceDTO>> getDevicesByModel(@RequestParam("model") String model) {
        List<Device> devices;
        try {
            devices = deviceService.findDeviceByModel(model);
            List<DeviceDTO> deviceDTO = new ArrayList<>();
            for (Device device : devices) {
                deviceDTO.add(new DeviceDTO(device.getId(),
                        device.getModel(),
                        device.getSerialnumber(),
                        device.getMac(),
                        device.getIp(),
                        device.getImageUrl()
                        )
                );
            }
            return new ResponseEntity<>(deviceDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint for POST requests to retrieving {@link Device} data by its ID.
     * The {@link Device} is validated before being added.
     *
     @param deviceId a Long value representing the {@link Device} ID
     @return a ResponseEntity object wrapping the retrieved {@link Device} object and an HTTP status code
     indicating whether the request was successful or not
     */
    @Operation(summary = "Get a Device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device Found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeviceDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content)})
    @RequestMapping(value = "/devices/{deviceId}", method = RequestMethod.GET)
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("deviceId") Long deviceId) {
        Device device;
        try {
            device = deviceService.findDeviceById(deviceId);
            DeviceDTO deviceDTO =(new DeviceDTO(device.getId(),
                    device.getModel(),
                    device.getSerialnumber(),
                    device.getMac(),
                    device.getIp(),
                    device.getImageUrl()
            ));
            return new ResponseEntity<>(deviceDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *  Endpoint for GET requests to retrieve all the {@link Device} instances from the database.
     *  @return ResponseEntity> contains all the devices found
     */
    @Operation(summary = "Get all devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all devices",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeviceDTO.class)) })})
    @RequestMapping(value = "/devices/all", method = RequestMethod.GET)
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.findAllDevices();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @Operation(summary = "Add a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeviceDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    public ResponseEntity<DeviceDTO> addDevice(@RequestBody DeviceDTO dto,
                                           BindingResult bindingResult) {
        deviceValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Device device = deviceService.addDevice(dto);
        DeviceDTO deviceDTO = map(device);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(deviceDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(deviceDTO);
    }

/**
 *  Endpoint for DELETE requests for a {@link Device} by its ID.
 *
 *  @param deviceId The ID of the {@link Device} to be deleted.
 *  @return ResponseEntity with status 200 and the deleted {@link DeviceDTO} in JSON format if the device was deleted
 *  successfully, or a ResponseEntity with status 404 if the device was not found.
 *
 */
    @Operation(summary = "Delete a device by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeviceDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content)})
    @RequestMapping(value = "/devices/{deviceId}", method = RequestMethod.DELETE)
    public ResponseEntity<DeviceDTO> deleteDevice(@PathVariable("deviceId") Long deviceId) {
        try {
            Device device = deviceService.findDeviceById(deviceId);
            deviceService.deleteDevice(deviceId);
            DeviceDTO DeviceDTO = map(device);
            return new ResponseEntity<>(DeviceDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     *
     * Endpoint for PUT requests to update a {@link Device} by its ID.
     *
     * @param deviceId the ID of the {@link Device} to be updated
     * @param dto the updated {@link Device} information in the form of a {@link DeviceDTO} object.
     * @param bindingResult the validation result of the {@link DeviceDTO}
     * @return a ResponseEntity with the updated  {@link Device} information in the form of a  {@link DeviceDTO}
     * and a status of 200 (OK) if successful, or with a status of 400 (Bad Request) if the
     * input was invalid or 404 (Not Found) if the device was not found
     */
    @Operation(summary = "Update a device")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeviceDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Device not found",
                    content = @Content) })
    @RequestMapping(value = "/devices/{deviceId}", method = RequestMethod.PUT)
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("deviceId") Long deviceId,
                                              @RequestBody DeviceDTO dto, BindingResult bindingResult) {
        deviceValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            dto.setId(deviceId);
            Device device = deviceService.updateDevice(dto);
            DeviceDTO deviceDTO = map(device);
            return new ResponseEntity<>(deviceDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Maps a {@link Device} to a {@link DeviceDTO}
     * @param device
     * @return
     */
    private DeviceDTO map(Device device) {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setModel(device.getModel());
        deviceDTO.setSerialnumber(device.getSerialnumber());
        deviceDTO.setMac(device.getMac());
        deviceDTO.setIp(device.getIp());
        deviceDTO.setImageUrl(device.getImageUrl());
        return deviceDTO;
    }

}
