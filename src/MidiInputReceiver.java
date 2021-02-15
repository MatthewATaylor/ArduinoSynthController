import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

class MidiInputReceiver implements Receiver {
    private static final int MIN_NOTE_ID = 53;

    private ArduinoSerialPort port;

    MidiInputReceiver(ArduinoSerialPort port) {
        this.port = port;
    }

    public void send(MidiMessage message, long timeStamp) {
        byte[] messageBytes = message.getMessage();

        String command;
        if (messageBytes[0] == -112) {
            command = "ON";
        }
        else if (messageBytes[0] == -128) {
            command = "OFF";
        }
        else {
            return;
        }

        int noteIndex = messageBytes[1] - MIN_NOTE_ID;
        command += " " + noteIndex;

        System.out.println("MIDI message: " + command);
        port.write(command);
    }

    public void close() {}
}
