package Logistic;

import AudioAndTexts.AudioPlayer;
import Grafics.Frame;
import Grafics.GifPanel;
import Grafics.HealthPanel;
import Grafics.Lobby;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.DriverPropertyInfo;
import java.util.*;
import java.util.stream.Collectors;

import static java.awt.Color.*;

public class Game {

    private ImageIcon NPC1ShootingGun;
    private ImageIcon NPC2ShootingGun;
    private ImageIcon NPC1ShootingSelf;
    private ImageIcon NPC2ShootingSelf;
    private ImageIcon background;
    private ImageIcon ManLeft;
    private ImageIcon ManRight;
    private JLabel backgroundLabel;
    private Player player1;
    private Player player2;
    private GifPanel gifPanel;
    private JLabel statusLabel;
    private JButton fireButton;
    private JButton shootSelfButton;
    private JButton inventoryButton;
    private JButton exitbutton;
    private int[] chamber;
    private int currentChamber;
    private int livesPlayer1;
    private int livesPlayer2;
    private boolean isPlayer1Turn;
    private Random random = new Random();
    private HealthPanel healthPanel;
    private Inventory inventoryPlayer1;
    private Inventory inventoryPlayer2;


    Frame frame = new Frame("Game", "Res/GameProfiles/GameProfileBIG.jpg");

    public Game(String playerName1, String playerName2, int numberOfLifes) {

        this.inventoryPlayer1 = new Inventory();
        this.inventoryPlayer2 = new Inventory();


        NPC1ShootingGun = new ImageIcon("Res/Gifs/NPCShootingGunRightLooping.gif");
        NPC2ShootingGun = new ImageIcon("Res/Gifs/NPCShootingGunLeftLooping.gif");
        NPC1ShootingSelf = new ImageIcon("Res/Gifs/shooot_self_gif_left_loop.gif");
        NPC2ShootingSelf = new ImageIcon("Res/Gifs/shooot_self_gif_right_loop.gif");
        background = new ImageIcon("Res/Backgrounds/GameBackGround1.png");

        backgroundLabel = new JLabel(background);
        backgroundLabel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundLabel);

        player1 = new Player(playerName1);
        player2 = new Player(playerName2);
        player1.setNumberOfLifes(numberOfLifes);
        player2.setNumberOfLifes(numberOfLifes);
        livesPlayer1 = numberOfLifes;
        livesPlayer2 = numberOfLifes;
        isPlayer1Turn = true;

//        Border border = BorderFactory.createLineBorder(GREEN);
//        Border border1 = BorderFactory.createLineBorder(YELLOW);


        healthPanel = new HealthPanel(numberOfLifes);
        healthPanel.setBounds(0, 525, 1920, 50); // domaci rozmery
        healthPanel.setOpaque(false);
        // healthPanel.setBorder(border1);
        frame.add(healthPanel);
        updateHealthDisplay();

        gifPanel = new GifPanel(NPC2ShootingGun, NPC1ShootingGun, NPC1ShootingSelf, NPC2ShootingSelf,
                background, ManLeft, ManRight);

        gifPanel.setOpaque(false);
        gifPanel.setBounds(0, 0, 1920, 1080);
        // gifPanel.setBorder(border);


        backgroundLabel.add(gifPanel, BorderLayout.CENTER);

        // Initialize exitbutton
        exitbutton = new JButton("Exit");
        exitbutton.setBackground(RED);
        exitbutton.addActionListener(e -> {
            frame.dispose();
            new Lobby();
        });

        run();
    }

    public void run() {
        statusLabel = new JLabel(player1.getName() + "'s turn. Lives: " + livesPlayer1 + " has " + inventoryPlayer1.getCigarettes() + " ciggarets ");
        statusLabel.setForeground(CYAN);
        fireButton = new JButton("Fire at Opponent");
        shootSelfButton = new JButton("Shoot Self");
        inventoryButton = new JButton("Check Inventory");





        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false);
        topPanel.add(statusLabel);
        topPanel.add(fireButton);
        topPanel.add(shootSelfButton);
        topPanel.add(inventoryButton);
        topPanel.add(exitbutton);
        backgroundLabel.add(topPanel, BorderLayout.NORTH);
        spinChamber();


        fireButton.addActionListener(e -> handleFire());
        shootSelfButton.addActionListener(e -> handleShootSelf());
        inventoryButton.addActionListener(e -> showInventoryOptions());
    }

    private void handleFire() {
        int delay1 = 3100;
        disableButtonsForDuration(3000); // Disable buttons for 3 seconds
        if (isPlayer1Turn) {
            gifPanel.showLeftGif(NPC2ShootingGun, delay1);
        } else {
            gifPanel.showRightGif(NPC1ShootingGun, delay1);
        }
        processTurn(isPlayer1Turn ? 2 : 1, player1.getName(), player2.getName());
    }

    private void handleShootSelf() {
        int delay1 = 3400;
        disableButtonsForDuration(3400); // Disable buttons for 3 seconds
        if (isPlayer1Turn) {
            gifPanel.showLeftGif(NPC1ShootingSelf, delay1);
        } else {
            gifPanel.showRightGif(NPC2ShootingSelf, delay1);
        }
        processSelfShot(player1.getName(), player2.getName());
    }

    private void disableButtonsForDuration(int duration) {
        disableButtons();
        Timer timer = new Timer(duration, e -> enableButtons());
        timer.setRepeats(false);
        timer.start();
    }

    private void enableButtons() {
        fireButton.setEnabled(true);
        shootSelfButton.setEnabled(true);
        inventoryButton.setEnabled(true);
    }


    private void processTurn(int targetPlayer, String name1, String name2) {
        Timer timer = new Timer(2000, e -> {
            if (chamber[currentChamber] == 1) {
                if (targetPlayer == 1) {
                    livesPlayer1--;
                    shootSound("Live");
                    updateHealthDisplay();
                    if (livesPlayer1 == 0) {
                        statusLabel.setText("BOOM! " + name1 + " is dead. Player 2 wins!");
                        whoWins();
                        disableButtons();
                        return;
                    } else {
                        statusLabel.setText("BOOM! " + name1 + " lost a life. Lives left: " + livesPlayer1);
                    }
                } else if (targetPlayer == 2) {
                    livesPlayer2--;
                    shootSound("Live");
                    updateHealthDisplay();
                    if (livesPlayer2 == 0) {
                        statusLabel.setText("BOOM! " + name2 + " is dead. Player 1 wins!");
                        whoWins();
                        disableButtons();
                        return;
                    } else {
                        statusLabel.setText("BOOM! " + name2 + " lost a life. Lives left: " + livesPlayer2);
                    }
                }
            } else {
                statusLabel.setText("Click! Player " + targetPlayer + " is safe.");
                shootSound("Blank");
            }

            currentChamber = (currentChamber + 1) % chamber.length; // Update the chamber position
            isPlayer1Turn = !isPlayer1Turn;
            updateTurnInfo(player1.getName(), player2.getName());
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void processSelfShot(String name1, String name2) {
        Timer timer = new Timer(2000, e -> {
            if (chamber[currentChamber] == 1) {
                if (isPlayer1Turn) {
                    livesPlayer1--;
                    shootSound("Live");
                    updateHealthDisplay();
                    setIsPlayer1Turn(false);
                    if (livesPlayer1 <= 0) {
                        statusLabel.setText("BOOM! " + name1 + " is dead. Player " + name2 + " wins!");
                        disableButtons();
                        whoWins();
                        return;
                    }
                } else {
                    livesPlayer2--;
                    shootSound("Live");
                    updateHealthDisplay();
                    if (livesPlayer2 <= 0) {
                        statusLabel.setText("BOOM! " + name2 + " is dead. Player " + name1 + " wins!");
                        disableButtons();
                        whoWins();
                        return;
                    }
                }
                statusLabel.setText("BOOM! You lost a life. Lives left: " + (isPlayer1Turn ? livesPlayer1 : livesPlayer2));
            } else {
                statusLabel.setText("Click! You are safe. You continue your turn.");
                shootSound("Blank");
            }
            currentChamber = (currentChamber + 1) % chamber.length; // Update the chamber position
            updateHealthDisplay();
            updateTurnInfo(player1.getName(), player2.getName());
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void updateTurnInfo(String name1, String name2) {
        String currentPlayer = isPlayer1Turn ? name1 : name2;
        int currentLives = isPlayer1Turn ? livesPlayer1 : livesPlayer2;
        statusLabel.setText(currentPlayer + "'s turn. Lives: " + currentLives);
    }


    private void spinChamber() {
        chamber = new int[12];
        int bulletCount = random.nextInt(6) + 2; // 2 - 6 bullets

        Set<Integer> usedIndexes = new HashSet<>();
        while (usedIndexes.size() < bulletCount) {
            int index = random.nextInt(chamber.length);
            if (!usedIndexes.contains(index)) {
                chamber[index] = 1;
                usedIndexes.add(index);
            }
        }

        currentChamber = random.nextInt(chamber.length);
        int ranDom = random.nextInt(2) + 3;
        for (int i = 0; i < ranDom; i++) {


            generateRandomItem(inventoryPlayer1);
            generateRandomItem(inventoryPlayer2);
        }
    }

    private void generateRandomItem(Inventory inventory) {
        int chance = random.nextInt(3); // 0, 1, or 2
        switch (chance) {
            case 0:
                inventory.addCigarette();
                break;
            case 1:
                inventory.addMagnifier();
                break;
            case 2:
                // No item
                break;
        }
    }

    private void disableButtons() {
        fireButton.setEnabled(false);
        shootSelfButton.setEnabled(false);
        inventoryButton.setEnabled(false);
    }

    public void shootSound(String soundName) {
        AudioPlayer audioPlayer = new AudioPlayer();
        if (soundName.equals("Live")) {
            audioPlayer.playSoundEffect("Res/Sounds/GunShot.wav");
        } else if (soundName.equals("Blank")) {
            audioPlayer.playSoundEffect("Res/Sounds/revolverBlankSound.wav");
        }
    }

    private void updateHealthDisplay() {
        healthPanel.updatePlayer1Health(livesPlayer1);
        healthPanel.updatePlayer2Health(livesPlayer2);
    }

    private void whoWins() {
        if (player1.isAlive()) {
            JOptionPane.showMessageDialog(frame, player2.getName() + " wins!");
        } else {
            JOptionPane.showMessageDialog(frame, player1.getName() + " wins!");
        }
    }

    private void setIsPlayer1Turn(boolean player1Turn) {
        this.isPlayer1Turn = isPlayer1Turn;
    }

    private void showInventoryOptions() {
        String[] options = {"Use Magnifier", "Use Cigarette"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an item:", "Inventory",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            useMagnifier();
        } else if (choice == 1) {
            useCigarette();
        }
    }

    private void useMagnifier() {
        Inventory currentInventory = isPlayer1Turn ? inventoryPlayer1 : inventoryPlayer2;
        try {
            currentInventory.useMagnifier();
            String bulletStatus = chamber[currentChamber] == 1 ? "LIVE round!" : "BLANK round.";
            JOptionPane.showMessageDialog(null, "The current chamber contains a: " + bulletStatus, "Magnifier", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null, "You don't have a magnifier!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void useCigarette() {
        Inventory currentInventory = isPlayer1Turn ? inventoryPlayer1 : inventoryPlayer2;
        try {
            currentInventory.useCigarette();
            if (isPlayer1Turn) {
                if (livesPlayer1 < player1.getNumberOfLifes()) {
                    livesPlayer1++;
                    statusLabel.setText(player1.getName() + livesPlayer1);
                } else {
                    statusLabel.setText(player1.getName() + "already has the maximum number of lives.");
                }
            } else {
                if (livesPlayer2 < player2.getNumberOfLifes()) {
                    livesPlayer2++;
                    statusLabel.setText(player2.getName() + " used the cigarette. Lives left: " + livesPlayer2);
                } else {
                    statusLabel.setText(player2.getName() +" already has the maximum number of lives.");
                }
            }
            updateHealthDisplay();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(null, "You don't have a cigarette to use!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}