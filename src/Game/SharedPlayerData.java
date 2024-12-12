package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Class to persist player data across levels
public class SharedPlayerData {
    private static SharedPlayerData instance;

    private int experience;
    private int health;
    private int stamina;
    private int coins; // Add coins
    private List<String> inventory;
    private boolean hasSword; // Track whether the sword is picked up
    private HashMap<String, Boolean> flags = new HashMap<>(); // To store flags like spell purchase status

    // Private constructor for singleton pattern
    private SharedPlayerData() {
        this.experience = 0;
        this.health = 100;
        this.stamina = 100;
        this.coins = 0; // Initialize coins
        this.inventory = new ArrayList<>();
        this.hasSword = false; // Initialize sword status
    }

    // Singleton instance getter
    public static SharedPlayerData getInstance() {
        if (instance == null) {
            instance = new SharedPlayerData();
        }
        return instance;
    }

    // Getters and setters
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public boolean hasSword() {
        return hasSword;
    }

    public void setHasSword(boolean hasSword) {
        this.hasSword = hasSword;
    }

    // Methods for managing flags

    // Set a flag with a key and value
    public void setFlag(String key, boolean value) {
        flags.put(key, value);
    }

    // Get a flag value; if the flag is not set, return the default value
    public boolean getFlag(String key, boolean defaultValue) {
        return flags.getOrDefault(key, defaultValue);
    }

    // Optional: Clear all flags (if needed in certain scenarios)
    public void clearFlags() {
        flags.clear();
    }

    public void reset() {
        this.experience = 0;
        this.health = 100;
        this.stamina = 100;
        this.coins = 0; // Reset coins
        this.inventory.clear();
        this.hasSword = false; // Reset sword
        this.flags.clear(); // Reset flags
    }

    private boolean hasPurchasedSpells = false;

public boolean HasPurchasedSpells() {
    return hasPurchasedSpells;
}

public void setHasPurchasedSpells(boolean hasPurchasedSpells) {
    this.hasPurchasedSpells = hasPurchasedSpells;
}


private boolean isSwordEquipped = false; // Tracks if the sword is equipped

public boolean isSwordEquipped() {
    return isSwordEquipped;
}

public void setSwordEquipped(boolean isSwordEquipped) {
    this.isSwordEquipped = isSwordEquipped;
}




    public void syncWithSharedPlayerData() {
        SharedPlayerData sharedData = SharedPlayerData.getInstance();
        this.hasPurchasedSpells = sharedData.getFlag("hasPurchasedSpells", false);
        System.out.println("Player synced with SharedPlayerData: hasPurchasedSpells = " + this.hasPurchasedSpells);
        isSwordEquipped = false; // Reset sword equip state
        System.out.println("Player synced: Inventory = " + inventory);
    }


    
}