package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;

// script for talking to walrus npc
// checkout the documentation website for a detailed guide on how this script works
public class SebScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{

            addText("Hey new player!");
            addText("Do you know how to play the game?", new String[] { "Yes", "No", "Exit"}); // "Yes! My ball is gone", "No, I'm good!"

        }});

        scriptActions.add(new ConditionalScriptAction() {{
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 0;
                    }
                });

                addScriptAction(new TextboxScriptAction() {{
                    addText("You are a quick learner!");
                    addText("Have fun playing the game!");
                }});
            }});
            
            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 1;
                    }
                });

                addScriptAction(new TextboxScriptAction("So the first thing you'll notice is the health and \n stamina bar in the top left"));
                addScriptAction(new TextboxScriptAction("There is also an inventory button in which you can \n see what items you picked up")); //to be added/interacted
                addScriptAction(new TextboxScriptAction("To pick up items, simply press 'E' and the item \n will disappear"));
                addScriptAction(new TextboxScriptAction("You can also press 'P' to pause the game!"));
                addScriptAction(new TextboxScriptAction("Those are pretty much the basics, if you need a\nreminder, im always here!"));
                addScriptAction(new TextboxScriptAction("Have fun!"));


            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 2;
                    }
                });

                scriptActions.add(new UnlockPlayerScriptAction());

            }});

            }});

        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}