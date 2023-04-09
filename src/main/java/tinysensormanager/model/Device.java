package tinysensormanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Java bean for {@link Device} instances stored in the database.
 * Implements Serializable to be able to be sent over the network.
 *
 * @author manokel01
 * @version 1.0.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DEVICES")
public class Device implements Serializable {
    /**
     * The id of the {@link Device}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;
    /**
     * The name of the {@link Device}.
     */
    @Column(name = "DEVICE_NAME", nullable = false)
    private String model;
    /**
     * The serial number of the {@link Device}.
     */
    @Column(name = "SERIAL_NUMBER")
    private String serialnumber;
    /**
     * The mac address of the {@link Device}.
     */
    @Column(name = "MAC_ADDRESS", unique = true)
    private String mac;
    /**
     * The ip address of the {@link Device}.
     */
    @Column(name = "IP_ADDRESS")
    private String ip;
    /**
     * The image url of the {@link Device}.
     */
    @Column(name = "MAC")
    private String imageUrl;

}
