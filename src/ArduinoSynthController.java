import gui.GraphicsPanel;
import gui.Window;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        final int LOOP_DELAY = 10;
        Timer timer = new Timer(LOOP_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicsPanel.repaint();
            }
        });
        timer.start();
    }
}
