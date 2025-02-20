package Grafics;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HealthPanel extends JPanel {
    private List<JLabel> player1Hearts;
    private List<JLabel> player2Hearts;
    private ImageIcon heartIcon;

    public HealthPanel(int numberOfLives) {
        setLayout(null); // Use absolute positioning
        heartIcon = new ImageIcon("Res/Pngs/heart_final.png"); // Replace with the correct path to the heart icon

        player1Hearts = new ArrayList<>();
        player2Hearts = new ArrayList<>();

        for (int i = 0; i < numberOfLives; i++) {
            JLabel heartLabel = new JLabel(heartIcon);
            heartLabel.setBounds(110 + i * 30, 30, 30, 30); // Adjust position as needed
            player1Hearts.add(heartLabel);
            add(heartLabel);
        }

        for (int i = 0; i < numberOfLives; i++) {
            JLabel heartLabel = new JLabel(heartIcon);
            heartLabel.setBounds(1690 + i * 30, 30, 30, 30); // Adjust position as needed
            player2Hearts.add(heartLabel);
            add(heartLabel);
        }

        setOpaque(false); // Make the panel transparent
    }

    public void updatePlayer1Health(int health) {
        for (int i = 0; i < player1Hearts.size(); i++) {
            player1Hearts.get(i).setVisible(i < health);
        }
    }

    public void updatePlayer2Health(int health) {
        for (int i = 0; i < player2Hearts.size(); i++) {
            player2Hearts.get(i).setVisible(i < health);
        }
    }
}
