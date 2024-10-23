package ScriptActions;

import Level.Map;
import Level.ScriptState;
import Maps.TestMap;


public class coinsScriptAction extends ScriptAction {

    private int amount;
    
    public coinsScriptAction(int amount) {
        this.amount = amount;
    }

    @Override
    public ScriptState execute() {

        if (map instanceof TestMap) {
            TestMap currentMap = (TestMap) map;

            currentMap.addinCheese(amount);
            System.out.println("coins in players wallet:" + amount);
        } else {

            System.out.println("test");

        }

        return ScriptState.COMPLETED;


    }
    

}
