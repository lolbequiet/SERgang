package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;
import ScriptActions.*;

import Game.AudioManager;
import Engine.Key;
import Engine.Keyboard;

// script for talking to bug npc
// checkout the documentation website for a detailed guide on how this script works
public class BugScript extends Script {

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

        scriptActions.add(new NPCLockScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            
            addText("Hello!");
            addText("Do you like bugs?", new String[] { "Yes", "No" });
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
                    addText("I knew you were cool!");
                    addText("I'm going to let you in on a little secret...\nPress 'e' to pick up rocks.");
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
                
                addScriptAction(new TextboxScriptAction("Oh...uh...awkward..."));
            }});
        }});

        scriptActions.add(new NPCUnlockScriptAction());
        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
