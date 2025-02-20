package Grafics;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public Frame(String frameName, String iconPath) {
        this.setTitle("Dead Man's Pull");
        this.setBackground(Color.BLACK);
        this.setName(frameName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null); // Use absolute positioning
        this.setIconImage(new ImageIcon(iconPath).getImage());
        this.setSize(1920, 1080); // Adjust size
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Method to add components to the frame
    public void addComponent(JComponent component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
        this.add(component);
    }

    // Method to set background image
    public void setBackgroundImage(String imagePath) {
        JLabel background = new JLabel(new ImageIcon(imagePath));
        background.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.setContentPane(background);
    }

    public void close() {
        this.dispose();
    }
}