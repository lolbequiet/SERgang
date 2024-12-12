package Screens;


import Engine.GraphicsHandler;
import Engine.Key;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Game.SharedPlayerData;
import Level.FlagManager;
import Level.Player;
import Maps.ShopMap;
import Maps.TestMap;
import Players.Cat;
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
   protected SpriteFont healthPotion, mace, sandwich, randomSpell, title, exit, instructions;
   protected int currentShopItem = 0;
   protected int keyPressTimer;
   protected TestMap testMap;
 


   private HashMap<String, Integer> inventory = new HashMap<>(); // Inventory for purchased items


   public ShopScreen(ScreenCoordinator screenCoordinator, TestMap testMap) {
       this.screenCoordinator = screenCoordinator;
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


       restorePlayerData();




       // UI Titles and Item Descriptions
       title = new SpriteFont("MIKE'S DELI (somewhat)", 600, 50, "Arial", 30, new Color(49, 207, 240));
       title.setOutlineColor(Color.black);
       title.setOutlineThickness(3);


       healthPotion = new SpriteFont("Potion - 5 doubloons", 650, 150, "Arial", 24, new Color(49, 207, 240));
       healthPotion.setOutlineColor(Color.black);
       healthPotion.setOutlineThickness(2);


       mace = new SpriteFont("Mace - 10 doubloons", 645, 275, "Arial", 24, new Color(49, 207, 240));
       mace.setOutlineColor(Color.black);
       mace.setOutlineThickness(2);


       sandwich = new SpriteFont("Sandwich - 15 doubloons", 640, 400, "Arial", 24, new Color(49, 207, 240));
       sandwich.setOutlineColor(Color.black);
       sandwich.setOutlineThickness(2);


       randomSpell = new SpriteFont("Spells - 40 doubloons",650 , 525, "Arial", 24, new Color(50, 255, 10));
       randomSpell.setOutlineColor(Color.BLUE.brighter());
       randomSpell.setOutlineThickness(2);


       exit = new SpriteFont("Press 'B' to exit the Shop", 1200, 700, "Arial", 20, new Color(0,0,0));
       exit.setOutlineColor(Color.CYAN.brighter());
       exit.setOutlineThickness(5);


       instructions = new SpriteFont("Press 'ENTER' to buy an Item!", 30, 700, "Arial", 20, new Color(0,0,0));
       instructions.setOutlineColor(Color.GREEN.brighter());
       instructions.setOutlineThickness(5);






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


       if (Keyboard.isKeyUp(Key.ENTER)) {
           keyLocker.unlockKey(Key.ENTER);
       }




       // Loop through menu items
       if (currentShopItem > 3) {
           currentShopItem = 0;
       } else if (currentShopItem < 0) {
           currentShopItem = 3;
       }


       // Highlight current shop item
       if (currentShopItem == 0) {
           healthPotion.setColor(Color.yellow);
           mace.setColor(Color.white);
           sandwich.setColor(Color.white);
           randomSpell.setColor(Color.white);
           //System.out.println(player.getCoins());
       } else if (currentShopItem == 1) {
           healthPotion.setColor(Color.white);
           mace.setColor(Color.yellow);
           sandwich.setColor(Color.white);
           randomSpell.setColor(Color.white);
           //System.out.println(player.getCoins());
       } else if (currentShopItem == 2) {
           healthPotion.setColor(Color.white);
           mace.setColor(Color.white);
           sandwich.setColor(Color.yellow);
           randomSpell.setColor(Color.white);
           //System.out.println(player.getCoins());
       } else if (currentShopItem == 3) {
           healthPotion.setColor(Color.white);
           mace.setColor(Color.white);
           sandwich.setColor(Color.white);
           randomSpell.setColor(Color.GREEN.brighter().brighter());


       }


       // Handle purchasing items
       if (Keyboard.isKeyDown(Key.ENTER) && !keyLocker.isKeyLocked(Key.ENTER)) {


           keyLocker.lockKey(Key.ENTER);


           if (currentShopItem == 0) { // Health Potion
               if (testMap.cashinOut(5)) {
                   player.subtractCoins(5);
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
           } else if (currentShopItem == 3) {
               if (testMap.cashinOut(40)) {
                   player.subtractCoins(40);
                   //im sure there will be logic for the spell here
                   System.out.println("you unlocked a spell, test it out by pressing 4");
               } else {
                   System.out.println("unfortunately you don't have enough");
               }
           }
       }


       // Exit the shop
       if (Keyboard.isKeyDown(Key.B) && !keyLocker.isKeyLocked(Key.B)) {
           savePlayerData();
           System.out.println("saveplayer is called");
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
       title.draw(graphicsHandler);
       background.draw(graphicsHandler);
       healthPotion.draw(graphicsHandler);
       mace.draw(graphicsHandler);
       sandwich.draw(graphicsHandler);
       randomSpell.draw(graphicsHandler);
       exit.draw(graphicsHandler);
       instructions.draw(graphicsHandler);
   }
  


   private void savePlayerData() {
       SharedPlayerData data = SharedPlayerData.getInstance();
       if (player != null) {
           data.setHealth(player.getHealth());
           data.setExperience(player.getExperience());
           data.setStamina(player.getStamina());
           data.setCoins(player.getCoins()); // Save coins
           data.setInventory(player.getInventory());
           data.setHasSword(((Cat) player).hasSword()); // Save sword status
       }
   }


   public void restorePlayerData() {
       SharedPlayerData data = SharedPlayerData.getInstance();
       player = new Cat(0,0);




       if (data != null) {
           player.setHealth(data.getHealth());
           player.setExperience(data.getExperience());
           player.setStamina(data.getStamina());
           player.getInventory().clear();
           player.getInventory().addAll(data.getInventory());
           player.addCoins(data.getCoins()); // Restore coins
           if (data.hasSword()) {
               ((Cat) player).pickUpSword(); // Restore sword
           }
       }
   }
}



