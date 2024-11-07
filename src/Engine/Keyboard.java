package Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * This class manages the state of keyboard inputs throughout the game.
 * It supports checking whether keys are pressed, released, or held down.
 */
public class Keyboard {

    // HashMaps to store the state of keys (pressed or released).
    private static final HashMap<Integer, Boolean> keyDown = new HashMap<>();
    private static final HashMap<Integer, Boolean> keyUp = new HashMap<>();

    // Maps custom Key enums to their respective keycodes.
    private static final EnumMap<Key, Integer> keyMap = buildKeyMap();

    // Listener to handle keyboard events.
    private static final KeyListener keyListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
            // Not used, but required by KeyListener interface.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // Set the key's state to down when pressed.
            int keyCode = e.getKeyCode();
            keyDown.put(keyCode, true);
            keyUp.put(keyCode, false);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // Set the key's state to up when released.
            int keyCode = e.getKeyCode();
            keyDown.put(keyCode, false);
            keyUp.put(keyCode, true);
        }
    };

    // Private constructor to prevent instantiation.
    private Keyboard() {}

    /**
     * Returns the KeyListener instance to be attached to the game window or canvas.
     */
    public static KeyListener getKeyListener() {
        return keyListener;
    }

    /**
     * Checks if a key defined by the custom Key enum is currently pressed.
     */
    public static boolean isKeyDown(Key key) {
        return keyDown.getOrDefault(keyMap.get(key), false);
    }

    /**
     * Checks if a key defined by the custom Key enum is currently not pressed.
     */
    public static boolean isKeyUp(Key key) {
        return keyUp.getOrDefault(keyMap.get(key), true);
    }

    /**
     * Checks if a key defined by a KeyEvent keycode is currently pressed.
     */
    public static boolean isKeyDown(int keyCode) {
        return keyDown.getOrDefault(keyCode, false);
    }

    /**
     * Checks if a key defined by a KeyEvent keycode is currently not pressed.
     */
    public static boolean isKeyUp(int keyCode) {
        return keyUp.getOrDefault(keyCode, true);
    }

    /**
     * Checks if multiple custom Key enums are pressed simultaneously.
     */
    public static boolean areKeysDown(Key[] keys) {
        for (Key key : keys) {
            if (!keyDown.getOrDefault(keyMap.get(key), false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if multiple custom Key enums are not pressed simultaneously.
     */
    public static boolean areKeysUp(Key[] keys) {
        for (Key key : keys) {
            if (!keyUp.getOrDefault(keyMap.get(key), false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Builds a map of custom Key enums to their respective keycodes.
     */
    private static EnumMap<Key, Integer> buildKeyMap() {
        return new EnumMap<>(Key.class) {{
            put(Key.UP, KeyEvent.VK_W);
            put(Key.DOWN, KeyEvent.VK_S);
            put(Key.LEFT, KeyEvent.VK_A);
            put(Key.RIGHT, KeyEvent.VK_D);
            put(Key.ENTER, KeyEvent.VK_ENTER);
            put(Key.SPACE, KeyEvent.VK_SPACE);
            put(Key.ESC, KeyEvent.VK_ESCAPE);
            put(Key.SHIFT, KeyEvent.VK_SHIFT);
            put(Key.A, KeyEvent.VK_A);
            put(Key.B, KeyEvent.VK_B);
            put(Key.C, KeyEvent.VK_C);
            put(Key.D, KeyEvent.VK_D);
            put(Key.E, KeyEvent.VK_E);
            put(Key.F, KeyEvent.VK_F);
            put(Key.G, KeyEvent.VK_G);
            put(Key.H, KeyEvent.VK_H);
            put(Key.I, KeyEvent.VK_I);
            put(Key.J, KeyEvent.VK_J);
            put(Key.K, KeyEvent.VK_K);
            put(Key.L, KeyEvent.VK_L);
            put(Key.M, KeyEvent.VK_M);
            put(Key.N, KeyEvent.VK_N);
            put(Key.O, KeyEvent.VK_O);
            put(Key.P, KeyEvent.VK_P);
            put(Key.Q, KeyEvent.VK_Q);
            put(Key.R, KeyEvent.VK_R);
            put(Key.S, KeyEvent.VK_S);
            put(Key.T, KeyEvent.VK_T);
            put(Key.U, KeyEvent.VK_U);
            put(Key.V, KeyEvent.VK_V);
            put(Key.W, KeyEvent.VK_W);
            put(Key.X, KeyEvent.VK_X);
            put(Key.Y, KeyEvent.VK_Y);
            put(Key.Z, KeyEvent.VK_Z);
            put(Key.ONE, KeyEvent.VK_1);
            put(Key.TWO, KeyEvent.VK_2);
            put(Key.THREE, KeyEvent.VK_3);
            put(Key.FOUR, KeyEvent.VK_4);
            put(Key.FIVE, KeyEvent.VK_5);
            put(Key.SIX, KeyEvent.VK_6);
            put(Key.SEVEN, KeyEvent.VK_7);
            put(Key.EIGHT, KeyEvent.VK_8);
            put(Key.NINE, KeyEvent.VK_9);
            put(Key.ZERO, KeyEvent.VK_0);
        }};
    }
}
