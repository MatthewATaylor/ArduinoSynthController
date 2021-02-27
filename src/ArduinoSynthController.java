import gui.GraphicsPanel;
import gui.Window;

import javax.swing.Timer;

public class ArduinoSynthController {
    public static void main(String[] args) {
        ArduinoSerialPort port = new ArduinoSerialPort();
        port.open();

        MidiHandler handler = new MidiHandler();
        handler.open("AKM320", port);

        final int WINDOW_WIDTH = 620;
        final int WINDOW_HEIGHT = 800;

        Window window = new Window(WINDOW_WIDTH, WINDOW_HEIGHT, "ArduinoSynthController");
        GraphicsPanel graphicsPanel = new GraphicsPanel(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.add(graphicsPanel);
        graphicsPanel.repaint();
        graphicsPanel.revalidate();
        graphicsPanel.grabFocus();
        port.setOscilloscope(graphicsPanel.getOscilloscope());

        Timer timer = new Timer(10, event -> {
            try {
                graphicsPanel.repaint();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        timer.start();
    }
}
