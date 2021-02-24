package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class GraphicsPanel extends JPanel {
    private final double WIDTH;
    private final double HEIGHT;

    private final int BUTTON_AREA_HEIGHT = 250;

    private Oscilloscope oscilloscope;
    private Button pauseButton;

    public GraphicsPanel(double width, double height) {
        super();

        WIDTH = width;
        HEIGHT = height;

        setBackground(GlobalColors.BACKGROUND);
        setFocusable(true);

        oscilloscope = new Oscilloscope(
                0, 0,
                WIDTH, HEIGHT - BUTTON_AREA_HEIGHT
        );
        addKeyListener(oscilloscope);

        final double BUTTON_WIDTH = 100;
        final double BUTTON_HEIGHT = 50;

        final double PAUSE_BUTTON_X = WIDTH / 2.0 - BUTTON_WIDTH / 2.0;
        final double PAUSE_BUTTON_Y = HEIGHT - BUTTON_AREA_HEIGHT / 2.0 - BUTTON_HEIGHT / 2.0;

        pauseButton = new Button(
                PAUSE_BUTTON_X, PAUSE_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT,
                GlobalColors.LIGHT_GRAY, GlobalColors.BUTTON_HIGHLIGHT,
                GlobalColors.BUTTON_TEXT, "Pause"
        );
        pauseButton.setOnClickedAction(() -> {
            oscilloscope.togglePaused();
            if (pauseButton.getText().equals("Pause")) {
                pauseButton.setText("Play");
            }
            else {
                pauseButton.setText("Pause");
            }
        });
        addMouseListener(pauseButton);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        oscilloscope.draw(g2D);

        // Button area
        g2D.setPaint(GlobalColors.DARK_GRAY);
        g2D.fill(new Rectangle2D.Double(0, HEIGHT - BUTTON_AREA_HEIGHT, WIDTH, BUTTON_AREA_HEIGHT));

        pauseButton.draw(g2D);
    }
}
