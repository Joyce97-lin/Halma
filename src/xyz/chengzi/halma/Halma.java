package xyz.chengzi.halma;

import xyz.chengzi.halma.view.GameFrame;
import xyz.chengzi.halma.view.Music;

import javax.swing.*;

public class Halma {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame mainFrame = new GameFrame();
            mainFrame.setVisible(true);
        });
        String filepath = "summer.wav";
        Music musicObject = new Music();
        musicObject.playMusic(filepath);

    }
}
