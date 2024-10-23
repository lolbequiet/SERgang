package Scripts.TestMap;

import java.util.ArrayList;

import Level.Script;

import ScriptActions.*;

public class CoinsCollectedScript extends Script {

@Override
public ArrayList<ScriptAction> loadScriptActions(){
    ArrayList<ScriptAction> scriptActions = new ArrayList<>();

    scriptActions.add(new coinsScriptAction(1));

    scriptActions.add(new TextboxScriptAction() {{

        addText("oh nah we rich");


    }});

    return scriptActions;
}



}