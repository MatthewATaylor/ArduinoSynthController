package gui;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends JFrame {
    public Window(int width, int height, String title) {
        super(title);
        getContentPane().setPreferredSize(new Dimension(width, height));
        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}