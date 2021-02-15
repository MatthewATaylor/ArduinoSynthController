import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

class MidiHandler {
    void open(String deviceName, ArduinoSerialPort port) {
        MidiDevice.Info[] allInfo = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : allInfo) {
            if (!deviceName.isEmpty() && !deviceName.equals(info.getName())) {
                continue;
            }

            try {
                MidiDevice device = MidiSystem.getMidiDevice(info);

                Transmitter transmitter = device.getTransmitter();
                transmitter.setReceiver(new MidiInputReceiver(port));

                device.open();
                System.out.println("Opened MIDI device: " + device.getDeviceInfo());
            }
            catch (MidiUnavailableException e) {
                System.out.println("MIDI unavailable for device: " + info);
            }
        }
    }
}
