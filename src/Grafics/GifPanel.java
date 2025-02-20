package Grafics;

import Logistic.Game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;

public class GifPanel extends JPanel {

    private JLabel leftLabel;  // Pro zobrazení GIFu vlevo
    private JLabel rightLabel; // Pro zobrazení GIFu vpravo
    ImageIcon leftMan = new ImageIcon("Res/Pngs/LeftManStandingStill.png");
    ImageIcon rightMan = new ImageIcon("Res/Pngs/RightManStandingStill.png");


    public GifPanel(ImageIcon NPC2ShootingGun, ImageIcon NPC1ShootingGun, ImageIcon NPC1ShootingSelf, ImageIcon NPC2ShootingSelf, ImageIcon imageIcon, ImageIcon manLeft, ImageIcon background) {

        setLayout(null);
        rightLabel = new JLabel();

        rightLabel.setBounds(1246,425,256,256); // domaci rozmery
        //rightLabel.setBounds(1600,550,256,256);
        rightLabel.setIcon(null); // Výchozí stav (prázdné)
        rightLabel.setIcon(rightMan);

        leftLabel = new JLabel();
        leftLabel.setBounds(46,425,256,256); // domaci rozmery
        //leftLabel.setBounds(20,550,256,256);
        leftLabel.setIcon(leftMan); // Výchozí stav (prázdné)

        // Přidání labelů do panelu

        add(leftLabel);
        add(rightLabel);

        // Pravý label umístěn vpravo
    }


    // Metoda pro zobrazení GIF na levé straně
    public void showLeftGif(ImageIcon gifImage, int delay) {
        leftLabel.setIcon(gifImage);
        rightLabel.setIcon(rightMan);
        Timer timer = new Timer(delay, e -> {
            leftLabel.setIcon(leftMan);
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Metoda pro zobrazení GIF na pravé straně
    public void showRightGif(ImageIcon gifImage, int delay) {
        rightLabel.setIcon(gifImage);
        leftLabel.setIcon(leftMan);
            Timer timer = new Timer(delay, e ->{
                rightLabel.setIcon(rightMan);
            });
            timer.setRepeats(false);
            timer.start();
    }

    // Metoda pro reset obrázků na pozadí nebo prázdný stav
    public void resetLabels() {
        leftLabel.setIcon(null);
        rightLabel.setIcon(null);
    }

}