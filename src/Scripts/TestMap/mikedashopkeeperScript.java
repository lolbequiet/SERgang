package Scripts.TestMap;

import java.util.ArrayList;

import Engine.ScreenManager;
import Game.ScreenCoordinator;
import Level.Script;
import Screens.ShopScreen;
import ScriptActions.*;
import Game.*;

// script for talking to bug npc
// checkout the documentation website for a detailed guide on how this script works
public class mikedashopkeeperScript extends Script {

    @Override
    public ArrayList<ScriptAction> loadScriptActions() {
        ArrayList<ScriptAction> scriptActions = new ArrayList<>();
        scriptActions.add(new LockPlayerScriptAction());

        scriptActions.add(new NPCLockScriptAction());

        scriptActions.add(new NPCFacePlayerScriptAction());

        scriptActions.add(new TextboxScriptAction() {{
            addText("YOOOOOO no way");
            addText("not gon lie bro, you got mad bread bro");
            addText("funds you know what I mean?");
            addText("aight im bugging but i run a deli");
            addText("deli but i lowkey sell weapons on the side, my fault...");
            addText("you want to check it out or nah", new String[] {"yea", "nah"});

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
                    addText("bet bet, slide");
                }});
                scriptActions.add(new UnlockPlayerScriptAction());
                scriptActions.add(new ChangeFlagScriptAction("VIPaccess", true));
                
                scriptActions.add(new CustomScriptAction() {
                    @Override 
                    public void customExecute() {
                        System.out.println("going to shop");
                        // screenCoordinator.setGameState(GameState.SHOP);
                        // ScreenManager.getInstance().setCurrentScreen(new ShopScreen(player));
                        ScreenManager.getScreenCoordinator().setGameStatePersist(GameState.SHOP);
                    }
                });

            }});

            addConditionalScriptActionGroup(new ConditionalScriptActionGroup() {{
                addRequirement(new CustomRequirement() {
                    @Override
                    public boolean isRequirementMet() {
                        int answer = outputManager.getFlagData("TEXTBOX_OPTION_SELECTION");
                        return answer == 1;
                    }
                });
                
                addScriptAction(new TextboxScriptAction("no hard feelings brodie, keep bag chasing"));
            }});
        }});

        scriptActions.add(new NPCUnlockScriptAction());
        scriptActions.add(new UnlockPlayerScriptAction());

        return scriptActions;
    }
}
