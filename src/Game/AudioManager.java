package Game;

import javax.sound.sampled.*;
import java.io.File;

public class AudioManager {

    // Play a sound effect once
    public static void playSound(String filePath) {
        play(filePath, false, 1.0f); // Default volume: 1.0 (100%)
    }

    // Play a sound in a loop
    public static void playLoop(String filePath) {
        play(filePath, true, 0.3f); // Default volume: 1.0 (100%)
    }

    public static void playSound(String filePath, float volume) {
        play(filePath, false, volume);
    }

    public static void playLoop(String filePath, float volume) {
        play(filePath, true, volume);
    }

    //play audio
    private static void play(String filePath, boolean loop, float volume) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.err.println("Audio file not found: " + filePath);
                return;
            }

            // Load audio file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Set volume
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(volume) * 20); 
            volumeControl.setValue(dB);

            // Play sound
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }

            System.out.println("Playing audio: " + filePath + (loop ? " (looping)" : ""));
        } catch (Exception e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }
}