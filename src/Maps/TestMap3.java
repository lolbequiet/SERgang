package Maps;

import Level.*;
import NPCs.*;
import Scripts.SimpleTextScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset2;
import Tilesets.CommonTileset3;
import Utils.Point;

import java.util.ArrayList;
import java.util.Random;

import EnhancedMapTiles.CollectableCoin;

public class TestMap3 extends Map {
    
    private int playerWallet;
    
    public TestMap3() {
        super("Map design1.txt", new CommonTileset3());
        this.playerStartPosition = getMapTile(33, 22).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

         // Add coins to the map
         CollectableCoin coin1 = new CollectableCoin(new Point(500, 500), 10);
         CollectableCoin coin2 = new CollectableCoin(new Point(800, 600), 10);
         enhancedMapTiles.add(coin1);
         enhancedMapTiles.add(coin2);

        return enhancedMapTiles;
    }

    

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        return npcs;
    }

    @Override
    public ArrayList<NPC> loadEnemies() {
        ArrayList<NPC> enemies = new ArrayList<>();
        Random random = new Random();

        return enemies;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        return triggers;
    }

    @Override
    public void loadScripts() {
    }

    public void addinCheese(int total) {
        playerWallet += total;
        System.out.println("New amount: " + playerWallet);
    }

    public boolean cashinOut(int total) {
        if (playerWallet >= total) {
            playerWallet -= total;
            System.out.println("Cashed out, your total: " + total);
            return true;
        } else {
            System.out.println("Not enough coins.");
            return false;
        }
    }
}
