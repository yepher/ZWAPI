
package zwapi.nodes;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import zwapi.ZWFrame;

/**
 * Model of the Z-Wave serial controller for communicating with the Z-Wave network
 * 
 * @author David Lemvigh
 * @author Andreas Møller
 */
public class ZWNetworkController extends ZWController {

    private InputStream in;

    private OutputStream out;

    private CommPort commPort;

    private SerialPort serialPort;

    /**
     * Constructor for the network controller. <br/>
     * Also opens the connection to the port, and input/output-streams.
     * 
     * @param id
     *            node ID on in the Z-Wave network
     * @param portName
     *            name of the port the serial device is connected to.
     *            Can be set to null or blank, in which case the default os port with be assumed.
     * @throws Exception
     *             wrong port, occupied port, IOExceptions, all that good jazz.
     */
    public ZWNetworkController(int id, String portName) throws Exception {
        super(id);

        if (portName == null || portName.equals("")) {
            if (System.getProperty("os.name").equals("Mac OS X")) {
                portName = "/dev/tty.PL2303-00001004";
            } else if (System.getProperty("os.name").equals("Linux")) {
                portName = "/dev/ttyUSB0";
            }
        }
        System.out.println(portName);
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            this.commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (this.commPort instanceof SerialPort) {
                this.serialPort = (SerialPort) this.commPort;
                this.serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                this.in = this.serialPort.getInputStream();
                this.out = this.serialPort.getOutputStream();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }

    }

    public InputStream getInputStream() {
        return this.in;
    }

    public OutputStream getOutputStream() {
        return this.out;
    }

    public void setEventListener(SerialPortEventListener listener) {
        try {
            this.serialPort.addEventListener(listener);
            this.serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException ex) {
            System.err.println("Couldn't add listener to z-wave input stream");
        }
    }

    public void send(byte[] data) {
        try {
            this.out.write(data);
        } catch (IOException ex) {
            System.err.println("Unable to send data:\n" + ZWFrame.bytes2string(data));
            ex.printStackTrace();
        }
    }

    public void setDefault() {
        ZWFrame zm = new ZWFrame(ZWFrame.REQUEST, ZWFrame.ZW_SET_DEFAULT, null);
        send(zm.getBytes());
    }

    /**
     * requests a version report from the network controller
     */
    public void getVersion() {
        ZWFrame zm = new ZWFrame(ZWFrame.REQUEST, ZWFrame.ZW_VERSION, null);
        send(zm.getBytes());
    }

    /**
     * Network controller broadcasts it's node information frame
     */
    public void sendNodeInfo() {
        byte[] payload = {ZWFrame.NODE_BROADCAST};
        ZWFrame zm = new ZWFrame(ZWFrame.REQUEST, ZWFrame.ZW_SEND_NODE_INFO, payload, true); //send node information
        send(zm.getBytes());
    }

    public void sendAck() {
        try {
            this.out.write(ZWFrame.ACK);
        } catch (IOException ex) {
            System.err.println("Unable to send Acknowledge");
            ex.printStackTrace();
        }
    }

    public void sendNak() {
        try {
            this.out.write(ZWFrame.NAK);
        } catch (IOException ex) {
            System.err.println("Unable to send Not-Acknowledged");
            ex.printStackTrace();
        }
    }

}
