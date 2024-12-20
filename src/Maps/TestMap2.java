package Maps;

import EnhancedMapTiles.CollectableCoin;
import EnhancedMapTiles.PushableRock;
import Level.*;
import NPCs.*;
import Scripts.SimpleTextScript;
import Tilesets.CommonTileset2;
import Utils.Point;

import java.util.ArrayList;
import java.util.Random;

public class TestMap2 extends Map {
    
    private int playerWallet;
    
    public TestMap2() {
        super("Map design2.txt", new CommonTileset2());
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

        for (MapTile tile : mapTiles) {
            if (tile.getTileIndex() == 3) {
                PushableRock pushableRock = new PushableRock(tile.getLocation());
                enhancedMapTiles.add(pushableRock);

                Point location = tile.getLocation();
                setMapTile(
                    Math.round(location.x / tileset.getScaledSpriteWidth()),
                    Math.round(location.y / tileset.getScaledSpriteHeight()),
                    tileset.getTile(0).build(location.x, location.y)
                );
            }
        }

        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs() {
        ArrayList<NPC> npcs = new ArrayList<>();

        // Add Overworld Villager NPC
        OverworldVillager overworldVillager = new OverworldVillager(1, getMapTile(25, 15).getLocation());
        overworldVillager.setInteractScript(new SimpleTextScript("Villager: Welcome to the Overworld!"));
        npcs.add(overworldVillager);

        // Add Bud NPC
        Bud bud = new Bud(2, getMapTile(30, 18).getLocation());
        bud.setInteractScript(new SimpleTextScript("Bud: Stay safe out there!"));
        npcs.add(bud);

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
        // Example: Adding interaction with a specific map tile
        getMapTile(15, 20).setInteractScript(new SimpleTextScript("This path leads to the village."));
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
