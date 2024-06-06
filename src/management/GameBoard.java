package management;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.Map;

public class GameBoard extends JPanel implements ActionListener {
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int coin_x;
    private int coin_y;
    private int score;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean paused = false;

    private Timer timer;
    private Image ball;
    private Image coin;
    private Image head;

    private Clip collisionSound; // Sound for collision
    private Clip coinSound; // Sound for coin pickup
    private Clip gameOverSound; // Sound for game over

    private String username;
    private Map<String, Integer> userData;

    public GameBoard(String username, Map<String, Integer> userData) {
        this.username = username;
        this.userData = userData;
        initBoard();
        loadCollisionSound();
        loadCoinSound();
        loadGameOverSound();
    }

    // Initialize the game board
    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    // Load the images for the game
    private void loadImages() {
        ball = new ImageIcon(getClass().getResource("/resources/dot.png")).getImage();
        coin = new ImageIcon(getClass().getResource("/resources/coin.png")).getImage();
        head = new ImageIcon(getClass().getResource("/resources/head.png")).getImage();
    }

    // Load the collision sound effect
    private void loadCollisionSound() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/resources/33556732_retro-game-collision-sound-1_by_stormwaveaudio_preview.wav"))) {
            collisionSound = AudioSystem.getClip();
            collisionSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load the coin pickup sound effect
    private void loadCoinSound() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/resources/EatingCoin.wav"))) {
            coinSound = AudioSystem.getClip();
            coinSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load the game over sound effect
    private void loadGameOverSound() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/resources/mixkit-arcade-retro-game-over-213.wav"))) {
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialize the game state
    private void initGame() {
        dots = 3;
        score = 0;
        inGame = true;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateCoin();

        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // Draw the game graphics
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (paused) {
            showPauseMenu(g);
        } else if (inGame) {
            doDrawing(g);
        } else {
            gameOver(g);
        }
    }

    // Show the pause menu
    private void showPauseMenu(Graphics g) {
        String[] lines = {
                "Game Paused",
                "Press SPACE to Resume",
                "Press Q to Quit"
        };
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);

        int y = B_HEIGHT / 2 - (lines.length * metr.getHeight() / 2);
        for (String line : lines) {
            g.drawString(line, (B_WIDTH - metr.stringWidth(line)) / 2, y);
            y += metr.getHeight();
        }
    }

    // Draw the game elements
    private void doDrawing(Graphics g) {
        g.drawImage(coin, coin_x, coin_y, this);

        for (int z = 0; z < dots; z++) {
            if (z == 0) {
                g.drawImage(head, x[z], y[z], this);
            } else {
                g.drawImage(ball, x[z], y[z], this);
            }
        }

        Toolkit.getDefaultToolkit().sync();

        // Display the score
        String scoreText = "Score: " + score;
        g.setColor(Color.white);
        g.drawString(scoreText, 10, 20);
    }

    // Show the game over screen
    private void gameOver(Graphics g) {
        String msg = "Game Over\nScore: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);

        String[] lines = msg.split("\n");
        int y = B_HEIGHT / 2 - (lines.length * metr.getHeight() / 2);
        for (String line : lines) {
            g.drawString(line, (B_WIDTH - metr.stringWidth(line)) / 2, y);
            y += metr.getHeight();
        }

        playGameOverSound(); // Play the game over sound effect

        int choice = JOptionPane.showOptionDialog(this, "Retry or Exit?",
                "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Retry", "Exit"}, "Retry");

        if (choice == JOptionPane.YES_OPTION) {
            initGame();
        } else {
            playGameOverSound(); // Play the game over sound again before exiting
            JOptionPane.showMessageDialog(this, "Goodbye, " + username + "!");
            exitGame();
        }
    }

    // Play the collision sound effect
    private void playCollisionSound() {
        if (collisionSound != null) {
            collisionSound.setFramePosition(0);
            collisionSound.start();
        }
    }

    // Play the coin pickup sound effect
    private void playCoinSound() {
        if (coinSound != null) {
            coinSound.setFramePosition(0);
            coinSound.start();
        }
    }

    // Play the game over sound effect
    private void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.setFramePosition(0);
            gameOverSound.start();
        }
    }

    // Check for collisions with walls or the worm itself
    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                playCollisionSound();
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
            playCollisionSound();
        }

        if (y[0] < 0) {
            inGame = false;
            playCollisionSound();
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
            playCollisionSound();
        }

        if (x[0] < 0) {
            inGame = false;
            playCollisionSound();
        }

        if (!inGame) {
            timer.stop();
        }
    }

    // Generate new coordinates for the coin
    private void locateCoin() {
        int r = (int) (Math.random() * RAND_POS);
        coin_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        coin_y = ((r * DOT_SIZE));
    }

    // Handle game events on each timer tick
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkCoin();
            checkCollision();
            move();
        }

        repaint();
    }

    // Handle keyboard input
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                paused = !paused;
                if (paused) {
                    timer.stop();
                } else {
                    timer.start();
                }
                repaint();
            }

            if (key == KeyEvent.VK_Q && paused) {
                System.exit(0);
            }

            if (!paused) {
                if ((key == KeyEvent.VK_A) && (!rightDirection)) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_D) && (!leftDirection)) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_W) && (!downDirection)) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }

                if ((key == KeyEvent.VK_S) && (!upDirection)) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }
        }
    }

    // Exit the game
    private void exitGame() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GameBoard.this);
        topFrame.dispose();
    }

    // Check if the worm has eaten the coin
    private void checkCoin() {
        if ((x[0] == coin_x) && (y[0] == coin_y)) {
            dots++;
            score++;
            playCoinSound();
            locateCoin();
        }
    }

    // Move the worm
    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }
}
