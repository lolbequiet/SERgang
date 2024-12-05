package Maps;

import Level.*;
import NPCs.*;
import Scripts.SimpleTextScript;
import Tilesets.CommonTileset3;
import Utils.Point;
import EnhancedMapTiles.CollectableCoin;
import NPCs.*;

import java.util.ArrayList;
import java.util.Random;

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

        // Add BossNPC at tile (18, 4)
        Point bossSpawnLocation = getMapTile(18, 4).getLocation();
        BossMob boss = new BossMob(bossSpawnLocation);
        npcs.add(boss);

        return npcs;
    }

    @Override
    public ArrayList<NPC> loadEnemies() {
        ArrayList<NPC> enemies = new ArrayList<>();
        Random random = new Random();

        // Spawn 5 WalrusMob enemies at random locations
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(40) * 32; // Random x-coordinate within map range
            int y = random.nextInt(30) * 32; // Random y-coordinate within map range
            WalrusMob walrusMob = new WalrusMob(new Point(x, y));
            enemies.add(walrusMob);
        }

        return enemies;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();

        // Example trigger for an event near the boss
        triggers.add(new Trigger(550, 120, 50, 50, new SimpleTextScript("You're approaching the boss!"), "bossArea"));

        return triggers;
    }

    @Override
    public void loadScripts() {
        // Example scripts for tiles
        getMapTile(18, 3).setInteractScript(new SimpleTextScript("This is the boss's domain."));
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
