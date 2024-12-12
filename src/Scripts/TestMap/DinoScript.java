package Scripts.TestMap;

import java.util.ArrayList;

import Builders.FrameBuilder;
import Builders.MapTileBuilder;
import Game.AudioManager;
import GameObject.Frame;
import Level.*;
import ScriptActions.*;

import Utils.Direction;
import Utils.Point;
import Utils.Visibility;

// script for talking to dino npc
// checkout the documentation website for a detailed guide on how this script works
public class DinoScript extends Script {

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

            addText("Hello Traveler");
            addText("Why do you come this way?", new String[] { "Info?", "Hi", "Exit"}); // "Yes! My ball is gone", "No, I'm good!"

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
                    addText("there are coins around the map you can collect \n to enter the shop to get gear!");

                    addScriptAction(new ChangeFlagScriptAction("hasTalkedToDinosaur", true));
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

                addScriptAction(new TextboxScriptAction("Caution traveling!"));


                addScriptAction(new ChangeFlagScriptAction("hasTalkedToWalrus", true));

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