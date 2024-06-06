
package management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WormGame extends JFrame {
    private BackgroundMusic backgroundMusic;
    private String username;
    private Map<String, Integer> userData;
    private final String DATA_FILE = "userData.dat";

    public WormGame() {
        loadUserData(); // Load user data at the start
        backgroundMusic = new BackgroundMusic("/resources/Old_Video_Game_Music_1_-_David_Fesliyan.wav");
        backgroundMusic.play(); // Play background music
        promptUsername(); // Prompt the user for their username
        showMenu(); // Show the main menu
    }

    // Prompt the user to enter their username
    private void promptUsername() {
        username = JOptionPane.showInputDialog(this, "Enter your username:", "Username", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            username = "Player";
        }
    }

    // Load user data from a file
    private void loadUserData() {
        userData = new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            userData = (Map<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File not found or other IO exception, starting fresh
        }
    }

    // Save user data to a file
    private void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Show the main menu
    private void showMenu() {
        String instructions = "Use W, A, S, D keys to move the worm:\n" +
                "W - Up\n" +
                "A - Left\n" +
                "S - Down\n" +
                "D - Right\n" +
                "Collect the coins to grow longer. Avoid running into the walls or yourself!\n" +
                "Press SPACE to pause/resume the game.\n" +
                "Press F to toggle full-screen mode.";
        JOptionPane.showMessageDialog(this, instructions, "How to Play", JOptionPane.INFORMATION_MESSAGE);

        String[] options = {"Start", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, "Welcome to Worm Game", "Worm Game Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            startGame(); // Start the game if "Start" is selected
        } else {
            exitGame(); // Exit the game if "Exit" is selected
        }
    }

    // Start the game
    private void startGame() {
        GameBoard gameBoard = new GameBoard(username, userData);
        add(gameBoard);
        gameBoard.requestFocus(); // Request focus for keyboard input
        setTitle("Worm Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Exit the game and save user data
    private void exitGame() {
        saveUserData();
        JOptionPane.showMessageDialog(this, "Thank you for playing!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WormGame());
    }
}
