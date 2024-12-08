package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;


// trigger script at beginning of game to set that heavy emotional plot
// checkout the documentation website for a detailed guide on how this script works
public class LostBallScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("Huh? What is this place?");
            addText("I was just in my room... how strange\nI've got to get out of here!");
            addText("Maybe that guy over there will know where it is!");
        }});

        scriptActions.add(new ChangeFlagScriptAction("hasLostBall", true));

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
