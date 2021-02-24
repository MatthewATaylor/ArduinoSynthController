import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;
import gui.Oscilloscope;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ArduinoSerialPort implements SerialPortMessageListener {
    private static final Charset CHARSET = StandardCharsets.US_ASCII;
    private static final byte[] DELIMITER = { '\r', '\n' };

    private SerialPort arduinoPort;
    private boolean isOpened = false;

    private Oscilloscope oscilloscope;

    private void listAvailablePorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Available ports:");
        for (SerialPort port : ports) {
            System.out.println(port.getDescriptivePortName());
        }
        System.out.println();
    }

    void setOscilloscope(Oscilloscope oscilloscope) {
        this.oscilloscope = oscilloscope;
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
        if (message.length() > 6 && message.substring(0, 3).equals("OSC")) {
            String[] messageSegments = message.split(" \\| ");
            if (messageSegments.length == 3) {
                double seconds = Double.parseDouble(messageSegments[1]) / 1000000;
                double voltage = Double.parseDouble(messageSegments[2]);
                if (oscilloscope != null) {
                    try {
                        oscilloscope.addPoint(seconds, voltage);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
