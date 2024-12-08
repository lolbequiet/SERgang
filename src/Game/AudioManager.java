package Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

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


    public static void play(String resourcePath, boolean loop, float volume) {
        try {
            // Load the audio resource using the class loader
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    AudioManager.class.getClassLoader().getResource(resourcePath)
            );
            if (audioStream == null) {
                System.err.println("Audio resource not found: " + resourcePath);
                return;
            }

            // Create and open the audio clip
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Set volume
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(volume) * 20); // Convert linear volume to decibels
            volumeControl.setValue(dB);

            // Play sound
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }

            System.out.println("Playing audio: " + resourcePath + (loop ? " (looping)" : ""));
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file format: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading audio resource: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Volume setting error: " + e.getMessage());
        }
    }

}