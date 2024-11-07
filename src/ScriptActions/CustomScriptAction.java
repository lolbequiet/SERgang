package ScriptActions;

import Level.ScriptState;
import Game.AudioManager;

// Abstract class for custom script actions
public abstract class CustomScriptAction extends ScriptAction {

    // Override the execute method to define custom behavior
    @Override
    public ScriptState execute() {
        customExecute(); // Call custom behavior
        return ScriptState.COMPLETED; // Indicate the script is completed
    }

    // Abstract method for subclasses to implement specific behavior
    public abstract void customExecute();
}