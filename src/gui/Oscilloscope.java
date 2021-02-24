package gui;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

class Oscilloscope {
    private static class Measurement {
        double x;
        double y;

        Measurement(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private final double X;
    private final double Y;
    private final double WIDTH;
    private final double HEIGHT;

    private boolean paused = false;

    private double xMin = 0;
    private double xMax = 2;
    private double yMin = 0;
    private double yMax = 5;

    private List<Measurement> measurements = new ArrayList<>();

    Oscilloscope(double x, double y, double width, double height) {
        X = x;
        Y = y;
        WIDTH = width;
        HEIGHT = height;

        for (double i = 0; i < 10; i += 0.1) {
            addPoint(i, i * 0.5);
        }
    }

    void draw(Graphics2D g2D) {
        // Voltage reading points
        final double Y_AXIS_BAR_WIDTH = 18;
        g2D.setPaint(GlobalColors.GREEN);
        for (Measurement measurement : measurements) {
            final double POINT_X = lerp(
                    measurement.x,
                    xMin, xMax,
                    X + Y_AXIS_BAR_WIDTH, X + WIDTH
            );
            final double POINT_Y = lerp(
                    measurement.y,
                    yMin, yMax,
                    Y + HEIGHT, Y
            );

            if (POINT_X < X || POINT_X > X + WIDTH || POINT_Y < Y || POINT_Y > Y + HEIGHT) {
                continue;
            }

            final double POINT_RADIUS = 4;
            g2D.fill(new Ellipse2D.Double(
                    POINT_X - POINT_RADIUS, POINT_Y - POINT_RADIUS,
                    POINT_RADIUS * 2, POINT_RADIUS * 2
            ));
        }

        // Y axis bar
        g2D.setPaint(GlobalColors.DARK_GRAY);
        g2D.fill(new Rectangle2D.Double(X, Y, Y_AXIS_BAR_WIDTH, HEIGHT));

        // Y axis scale
        g2D.setPaint(GlobalColors.LIGHT_GRAY);
        g2D.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        final double SCALE_INTERVAL = HEIGHT / (yMax - yMin);
        for (int i = Math.round((float)yMin); i <= Math.round((float)yMax); ++i) {
            final String SCALE_TEXT = String.valueOf(i);
            final int FONT_WIDTH = g2D.getFontMetrics().stringWidth(SCALE_TEXT);
            g2D.drawString(
                    SCALE_TEXT,
                    (int)(Y_AXIS_BAR_WIDTH / 2.0 - FONT_WIDTH / 2.0),
                    (int)(Y + HEIGHT - (i - yMin) * SCALE_INTERVAL)
            );
        }
    }

    void addPoint(double time, double voltage) {
        if (paused) {
            return;
        }

        measurements.add(new Measurement(time, voltage));

        // Auto-shift scope
        if (time > xMax) {
            final double X_AXIS_SHIFT = time - xMax;
            xMin += X_AXIS_SHIFT;
            xMax += X_AXIS_SHIFT;
        }

        // Limit number of stored measurements
        if (measurements.size() > 10000) {
            measurements.remove(0);
        }
    }

    void shiftX(double amount) {
        xMin += amount;
        xMax += amount;
    }

    void shiftY(double amount) {
        yMin += amount;
        yMax += amount;
    }

    void zoomX(double amount) {
        xMin += amount;
        xMax -= amount;

        // Zoomed too far
        if (xMin >= xMax) {
            xMin -= amount;
            xMax += amount;
        }
    }

    void zoomY(double amount) {
        yMin += amount;
        yMax -= amount;

        // Zoomed too far
        if (yMin >= yMax) {
            yMin -= amount;
            yMax += amount;
        }
    }

    void togglePaused() {
        paused = !paused;
    }

    private static double lerp(
            double value,
            double sourceMin, double sourceMax,
            double destinationMin, double destinationMax
    ) {
        return (value - sourceMin) / (sourceMax - sourceMin) *
                (destinationMax - destinationMin) + destinationMin;
    }
}
