package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

class Button implements MouseListener {
    private final double X;
    private final double Y;
    private final double WIDTH;
    private final double HEIGHT;

    private Color backgroundColor;
    private Color highlightColor;
    private Color textColor;
    private String text;

    private Runnable onClickedAction = () -> {};
    private Runnable onPressedAction = () -> {};

    private boolean isPressed = false;

    private boolean mouseIsOverButton(Point mousePosition) {
        return mousePosition.x > X && mousePosition.x < X + WIDTH &&
                mousePosition.y > Y && mousePosition.y < Y + HEIGHT;
    }

    Button(
            double x, double y, double width, double height,
            Color backgroundColor, Color highlightColor, Color textColor,
            String text
    ) {
        X = x;
        Y = y;
        WIDTH = width;
        HEIGHT = height;

        this.backgroundColor = backgroundColor;
        this.highlightColor = highlightColor;
        this.textColor = textColor;
        this.text = text;
    }

    void draw(Graphics2D g2D) {
        if (isPressed) {
            g2D.setPaint(highlightColor);
            onPressedAction.run();
        }
        else {
            g2D.setPaint(backgroundColor);
        }

        g2D.fill(new Rectangle2D.Double(X, Y, WIDTH, HEIGHT));

        g2D.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        final int FONT_WIDTH = g2D.getFontMetrics().stringWidth(text);
        final int FONT_HEIGHT = g2D.getFontMetrics().getHeight();

        g2D.setPaint(textColor);
        g2D.drawString(
                text,
                (float)(X + WIDTH / 2.0 - FONT_WIDTH / 2.0),
                (float)(Y + HEIGHT / 2.0 + FONT_HEIGHT / 4.0)
        );
    }

    void setOnClickedAction(Runnable onClickedAction) {
        this.onClickedAction = onClickedAction;
    }

    void setOnPressedAction(Runnable onPressedAction) {
        this.onPressedAction = onPressedAction;
    }

    String getText() {
        return text;
    }
    void setText(String text) {
        this.text = text;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mouseIsOverButton(e.getPoint())) {
            onClickedAction.run();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        isPressed = mouseIsOverButton(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
