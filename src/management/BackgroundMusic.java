package management;

import javax.sound.sampled.*;

public class BackgroundMusic {
    private Clip clip;

    public BackgroundMusic(String musicFilePath) {
        try {
            // Pass the file path as a string
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(musicFilePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

