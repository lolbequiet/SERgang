package Scripts.TestMap;

import java.util.ArrayList;

import Game.AudioManager;
import Level.Script;
import ScriptActions.*;

// script for talking to seb/walrus npc
// checkout the documentation website for a detailed guide on how this script works
public class SebScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
                scriptActions.add(new CustomScriptAction() {
            @Override
            public void customExecute() {
                AudioManager.playSound("Resources/Audio/ccinteract.wav");
                System.out.println("Interaction sound played.");
            }
        });
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{

            addText("Hey player!");
            addText("Do you know who i am?", new String[] { "Yes", "No", "Exit"}); // "Yes! My ball is gone", "No, I'm good!"

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
                    addText("Oh...");
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

                addScriptAction(new TextboxScriptAction("So you can attack mobs by pressing space! \n just thought i should remind you."));
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