package Maps;

import EnhancedMapTiles.CollectableCoin;
import EnhancedMapTiles.PushableRock;
import EnhancedMapTiles.Sword;
import Level.*;
import NPCs.*;
import Scripts.SimpleTextScript;
import Scripts.TestMap.*;
import Tilesets.CommonTileset;
import Utils.Point;

import java.util.ArrayList;
import java.util.Random;

public class TestMap extends Map {

    private int playerWallet;  // Track the player's wallet (coins collected)

    public TestMap() {
        super("test_map.txt", new CommonTileset());
        this.playerStartPosition = getMapTile(17, 20).getLocation();
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();

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

        // Add a Sword to the map at tile (10,10)
        Sword sword = new Sword(getMapTile(10, 10).getLocation());
        enhancedMapTiles.add(sword);

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

        Walrus walrus = new Walrus(1, getMapTile(4, 28).getLocation().subtractY(40));
        walrus.setInteractScript(new WalrusScript());
        npcs.add(walrus);

        Dinosaur dinosaur = new Dinosaur(2, getMapTile(13, 4).getLocation());
        dinosaur.setExistenceFlag("hasTalkedToDinosaur");
        dinosaur.setInteractScript(new DinoScript());
        npcs.add(dinosaur);

        Seb seb = new Seb(3, getMapTile(5, 15).getLocation().subtractY(40));
        seb.setInteractScript(new WalrusScript());
        npcs.add(seb);

        Bug bug = new Bug(4, getMapTile(7, 12).getLocation().subtractX(20));
        bug.setInteractScript(new BugScript());
        npcs.add(bug);

        Bug beetle = new Bug(5, getMapTile(15, 22).getLocation().subtractX(30));
        beetle.setInteractScript(new BugScript());
        npcs.add(beetle);

        Seb ant = new Seb(6, new Point(14 * 32, 18 * 32).subtractX(30));
        ant.setInteractScript(new BugScript());
        npcs.add(ant);

        return npcs;
    }

    @Override
    public ArrayList<NPC> loadEnemies() {
        ArrayList<NPC> enemies = new ArrayList<>();
        Random random = new Random();

        // Spawn 5 WalrusMob enemies at random coordinates
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(40) * 32;  // Random x-coordinate within map range
            int y = random.nextInt(30) * 32;  // Random y-coordinate within map range
            WalrusMob walrusMob = new WalrusMob(new Point(x, y));
            enemies.add(walrusMob);
        }

        return enemies;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();

        triggers.add(new Trigger(790, 1030, 100, 10, new LostBallScript(), "hasLostBall"));
        triggers.add(new Trigger(790, 960, 10, 80, new LostBallScript(), "hasLostBall"));
        triggers.add(new Trigger(890, 960, 10, 80, new LostBallScript(), "hasLostBall"));

        return triggers;
    }

    @Override
    public void loadScripts() {
        getMapTile(21, 19).setInteractScript(new SimpleTextScript("Cat's house"));
        getMapTile(7, 26).setInteractScript(new SimpleTextScript("Walrus's house"));
        getMapTile(20, 4).setInteractScript(new SimpleTextScript("Dino's house"));
        getMapTile(2, 6).setInteractScript(new TreeScript());
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
