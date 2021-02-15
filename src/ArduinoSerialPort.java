import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ArduinoSerialPort implements SerialPortMessageListener {
    private static final Charset CHARSET = StandardCharsets.US_ASCII;
    private static final byte[] DELIMITER = { '\r', '\n' };

    private SerialPort arduinoPort;
    private boolean isOpened = false;

    @Override
    public byte[] getMessageDelimiter() {
        return DELIMITER;
    }

    @Override
    public boolean delimiterIndicatesEndOfMessage() {
        return true;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        String message = new String(serialPortEvent.getReceivedData(), CHARSET);
        message = message.substring(0, message.length() - DELIMITER.length);
        System.out.println("Serial input: " + message);
    }

    void open() {
        listAvailablePorts();
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            if (!port.getDescriptivePortName().contains("Arduino")) {
                continue;
            }

            System.out.println("Opening port: " + port.getDescriptivePortName());
            port.openPort();
            port.setBaudRate(115200);
            arduinoPort = port;

            port.addDataListener(this);
        }
        isOpened = true;
    }

    void close() {
        if (isOpened) {
            arduinoPort.removeDataListener();
            arduinoPort.closePort();
        }
        else {
            System.out.println("Failed to close unopened port");
        }
    }

    void write(String message) {
        if (isOpened) {
            message += '\n';
            byte[] bytes = message.getBytes(CHARSET);
            arduinoPort.writeBytes(bytes, bytes.length);
            System.out.print("Serial output: " + message);
        }
        else {
            System.out.println("Failed to write to unopened port");
        }
    }

    private void listAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available ports:");
        for (SerialPort port : ports) {
            System.out.println(port.getDescriptivePortName());
        }
        System.out.println();
    }
}
