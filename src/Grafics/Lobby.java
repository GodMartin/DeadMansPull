package Grafics;

import AudioAndTexts.AudioPlayer;
import Logistic.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

public class Lobby {
    private JPanel buttonPanel;
    private JPanel textPanel;
    private JButton startButton;
    private JButton exitButton;
    private JButton muteMusicButton;
    private JLabel backgroundLabel;
    private JLabel textLabel;
    private AudioPlayer audioPlayer;
    private Frame frame;
    public static int numberOfLifes;


    public static String player1Name;
    public static String player2Name;

    public Lobby() {
        frame = new Frame("Lobby", "Res/GameProfiles/GameProfileBIG.jpg");

        ImageIcon background = new ImageIcon("Res/Backgrounds/LobbyBackgroundFullScr.png");

        backgroundLabel = new JLabel(background);
        backgroundLabel.setLayout(new GridBagLayout());
        frame.setContentPane(backgroundLabel);


        // Vytvoření textového labelu
        textLabel = new JLabel("Dead Mans Pull", SwingConstants.CENTER);
        textLabel.setForeground(new Color(250, 160, 0)); // Zlato
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/AudioAndTexts/LobbyFont.TTF")).deriveFont(150f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            textLabel.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            textLabel.setFont(new Font("Arial", Font.BOLD, 50)); // Záložní font
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.NORTH;
        backgroundLabel.add(textLabel, gbc);

        // Vytvoření panelu s tlačítky
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tlačítka Start a Exit
        startButton = new JButton(resizeIcon("Res/Buttons/StartButtonImage.png", 200, 50));
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        exitButton = new JButton(resizeIcon("Res/Buttons/ExitButton.png", 200, 50));
        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);

        // Přidání akčních posluchačů
        startButton.addActionListener(e -> PreGame());
        exitButton.addActionListener(e -> {
            frame.dispose();
            audioPlayer.stopBackgroundMusic();
        });

        // Přidání tlačítek do panelu
        buttonPanel.add(startButton, gbc);
        buttonPanel.add(exitButton, gbc);

        // Umístění panelu s tlačítky pod textem
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundLabel.add(buttonPanel, gbc);

        // Mute tlačítko vpravo dole
        muteMusicButton = new JButton(resizeIcon("Res/Buttons/Mute.png", 50, 50));
        muteMusicButton.setPreferredSize(new Dimension(50, 50));
        muteMusicButton.setContentAreaFilled(false);
        muteMusicButton.setBorderPainted(false);
        muteMusicButton.addActionListener(e -> audioPlayer.muteBackgroundMusic());

        gbc = new GridBagConstraints();
        gbc.gridx = 1; // Do druhé sloupce (pravá strana)
        gbc.gridy = 1; // Do spodní části
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Vpravo dole
        gbc.insets = new Insets(0, 0, 10, 10); // Odsazení od okraje
        backgroundLabel.add(muteMusicButton, gbc);

        //Inicializace a přehrávání pozadí hudby
        audioPlayer = new AudioPlayer();
        audioPlayer.playBackgroundMusic("Res/Sounds/BackGroundMusic1.wav");

        frame.setVisible(true);
    }

    private void PreGame() {
        // Odebereme pouze start a exit tlačítka, mute zůstane
        buttonPanel.remove(startButton);
        buttonPanel.remove(exitButton);
        buttonPanel.revalidate();
        buttonPanel.repaint();

        // Vytvoříme panel pro vstupy hráčů
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nápisy a pole pro vstup hráčů
        JLabel player1Label = new JLabel("Player 1 Name:");
        player1Label.setForeground(Color.WHITE);
        JTextField player1Field = new JTextField(15);

        JLabel player2Label = new JLabel("Player 2 Name:");
        player2Label.setForeground(Color.WHITE);
        JTextField player2Field = new JTextField(15);

        JLabel livesLabel = new JLabel("Number of Lives:");
        livesLabel.setForeground(Color.WHITE);
        JSpinner livesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1)); // Výchozí životy: 3

        JButton submitButton = new JButton("Start Game");
        submitButton.addActionListener(e -> {
            player1Name = player1Field.getText();
            player2Name = player2Field.getText();
            numberOfLifes = (int) livesSpinner.getValue();

            // Spustíme hru s danými údaji
            new Game(player1Name, player2Name, numberOfLifes);
            frame.dispose();
            audioPlayer.stopBackgroundMusic();

        });

        // Přidáme komponenty do inputPanel
        inputPanel.add(player1Label, gbc);
        inputPanel.add(player1Field, gbc);
        inputPanel.add(player2Label, gbc);
        inputPanel.add(player2Field, gbc);
        inputPanel.add(livesLabel, gbc);
        inputPanel.add(livesSpinner, gbc);
        inputPanel.add(submitButton, gbc);

        // Přidáme inputPanel do buttonPanel pod mute tlačítko
        gbc.gridy = 1; // Umístíme pod mute tlačítko
        buttonPanel.add(inputPanel, gbc);

        // Aktualizujeme rozložení
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private ImageIcon resizeIcon(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img == null) {
                throw new IOException("Image file not found: " + path);
            }
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}