package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.FlagManager;
import Level.Player;
import Maps.ShopMap;
import Maps.TestMap;
import SpriteFont.SpriteFont;
import Engine.KeyLocker;
import Engine.Keyboard;

import java.awt.Color;
import java.util.HashMap;

public class ShopScreen extends Screen {

    protected FlagManager flagManager;
    protected ShopMap background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected Player player;
    protected ScreenCoordinator screenCoordinator;
    protected SpriteFont healthPotion, mace, sandwich;
    protected int currentShopItem = 0;
    protected int keyPressTimer;
    protected TestMap testMap;

    private HashMap<String, Integer> inventory = new HashMap<>(); // Inventory for purchased items

    public ShopScreen(ScreenCoordinator screenCoordinator, Player player, TestMap testMap) {
        this.screenCoordinator = screenCoordinator;
        this.player = player;
        this.testMap = testMap;

        initialize();
    }


    //test class, might not need it
    //public void itemPurchased(int cost){
    //    player.subtractCoins(cost);
   // }

    @Override
    public void initialize() {
        background = new ShopMap();
        background.setAdjustCamera(false);

        // UI Titles and Item Descriptions
        SpriteFont title = new SpriteFont("MIKE'S DELI", 50, 50, "Arial", 30, new Color(49, 207, 240));
        title.setOutlineColor(Color.black);
        title.setOutlineThickness(3);

        healthPotion = new SpriteFont("Potion - 5 doubloons", 30, 150, "Arial", 24, new Color(49, 207, 240));
        healthPotion.setOutlineColor(Color.black);
        healthPotion.setOutlineThickness(2);

        mace = new SpriteFont("Mace - 10 doubloons", 30, 200, "Arial", 24, new Color(49, 207, 240));
        mace.setOutlineColor(Color.black);
        mace.setOutlineThickness(2);

        sandwich = new SpriteFont("Sandwich - 15 doubloons", 30, 250, "Arial", 24, new Color(49, 207, 240));
        sandwich.setOutlineColor(Color.black);
        sandwich.setOutlineThickness(2);

        // Initialize inventory
        inventory.put("Potion", 0);
        inventory.put("Sandwich", 0);

        keyPressTimer = 0;
        keyLocker.lockKey(Key.B);
    }

    @Override
    public void update() {
        // Navigate the shop menu
        if (Keyboard.isKeyDown(Key.DOWN) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentShopItem++;
        } else if (Keyboard.isKeyDown(Key.UP) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentShopItem--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // Loop through menu items
        if (currentShopItem > 2) {
            currentShopItem = 0;
        } else if (currentShopItem < 0) {
            currentShopItem = 2;
        }

        // Highlight current shop item
        if (currentShopItem == 0) {
            healthPotion.setColor(Color.yellow);
            mace.setColor(Color.white);
            sandwich.setColor(Color.white);
            //System.out.println(player.getCoins());
        } else if (currentShopItem == 1) {
            healthPotion.setColor(Color.white);
            mace.setColor(Color.yellow);
            sandwich.setColor(Color.white);
            //System.out.println(player.getCoins());
        } else if (currentShopItem == 2) {
            healthPotion.setColor(Color.white);
            mace.setColor(Color.white);
            sandwich.setColor(Color.yellow);
            //System.out.println(player.getCoins());
        }

        // Handle purchasing items
        if (Keyboard.isKeyDown(Key.ENTER)) {
            if (currentShopItem == 0) { // Health Potion
                if (testMap.cashinOut(5)) {

                    System.out.println("the amount before you purchased the item:" + player.getCoins());

                    player.subtractCoins(5);

                    System.out.println("the amount after you purchased the item:" + player.getCoins());

                    inventory.put("Potion", inventory.get("Potion") + 1);
                    System.out.println("Potion purchased! Total potions: " + inventory.get("Potion"));
                } else {
                    System.out.println("Not enough coins for Potion!");
                }
            } else if (currentShopItem == 1) { // Mace
                if (testMap.cashinOut(10)) {
                    player.subtractCoins(10);
                    System.out.println("Mace purchased! (Not implemented in inventory)");
                } else {
                    System.out.println("Not enough coins for Mace!");
                }
            } else if (currentShopItem == 2) { // Sandwich
                if (testMap.cashinOut(15)) {
                    player.subtractCoins(15);
                    inventory.put("Sandwich", inventory.get("Sandwich") + 1);
                    System.out.println("Sandwich purchased! Total sandwiches: " + inventory.get("Sandwich"));
                } else {
                    System.out.println("Not enough coins for Sandwich!");
                }
            }
        }

        // Exit the shop
        if (Keyboard.isKeyDown(Key.B) && !keyLocker.isKeyLocked(Key.B)) {
            screenCoordinator.BackToPersist();
            keyLocker.lockKey(Key.B);
        }

        if (Keyboard.isKeyUp(Key.B)) {
            keyLocker.unlockKey(Key.B);
        }

        // Handle using items
        if (Keyboard.isKeyDown(Key.TWO)) { // Use Potion
            if (inventory.get("Potion") > 0) {
                player.setHealth(player.getMaxHealth()); // Restore health to max
                inventory.put("Potion", inventory.get("Potion") - 1);
                System.out.println("Potion used! Health restored. Remaining potions: " + inventory.get("Potion"));
            } else {
                System.out.println("No potions left!");
            }
        }

        if (Keyboard.isKeyDown(Key.THREE)) { // Use Sandwich
            if (inventory.get("Sandwich") > 0) {
                player.setStamina(player.getMaxStamina()); // Restore stamina to max
                inventory.put("Sandwich", inventory.get("Sandwich") - 1);
                System.out.println("Sandwich used! Stamina restored. Remaining sandwiches: " + inventory.get("Sandwich"));
            } else {
                System.out.println("No sandwiches left!");
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, 1500, 800, new Color(10, 10, 10, 150));
        background.draw(graphicsHandler);
        healthPotion.draw(graphicsHandler);
        mace.draw(graphicsHandler);
        sandwich.draw(graphicsHandler);
    }
}
