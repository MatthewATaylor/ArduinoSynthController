public class ArduinoSynthController {
    public static void main(String[] args) {
        ArduinoSerialPort port = new ArduinoSerialPort();
        port.open();

        MidiHandler handler = new MidiHandler();
        handler.open("AKM320", port);
    }
}
