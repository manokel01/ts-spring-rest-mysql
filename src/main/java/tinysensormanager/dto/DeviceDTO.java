package tinysensormanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tinysensormanager.model.Device;

import javax.validation.constraints.Pattern;

/**
 * This is a data transfer object (DTO) that represents a {@link Device}.
 *
 * <p>The {@link #id} field uniquely identifies the device.</p>
 * <p>The {@link #model} field specifies the device's model name or number.</p>
 * <p>The {@link #serialnumber} field specifies the device's serial number.</p>
 * <p>The {@link #mac} field specifies the device's MAC address.</p>
 * <p>The {@link #ip} field specifies the device's IP address.</p>
 * <p>The {@link #imageUrl} field specifies the URL of the device's image.</p>
 *
 * <p>Note that the {@link #mac} and {@link #ip} fields are annotated with regular expression patterns
 * to ensure they contain valid MAC and IP addresses, respectively.</p>
 *
 * @author Your Name
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    /**
     * The ID of the device.
     */
    private Long id;
    /**
     * The model name or number of the device.
     */
    private String model;
    /**
     * The serial number of the device.
     */
    private String serialnumber;
    /**
     * The MAC address of the device.
     *
     */
    @Pattern(regexp = "regex = “^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})|([0-9a-fA-F]{4}\\\\.[0-9a-fA-F]{4}\\\\.[0-9a-fA-F]{4})$")
    private String mac;

    /**
     * The IP address of the device.
     */
    @Pattern(regexp = "regex = “^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")
    private String ip;
    /**
     * The URL of the device's image.
     */
    private String imageUrl;

}
