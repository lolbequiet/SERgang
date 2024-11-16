package Maps;

//not sure if this class was needed

import Level.*;
import Tilesets.CommonTileset;
import java.util.ArrayList;

public class ShopMap extends Map{

    public ShopMap() {
        super("shop_map.txt", new CommonTileset());
    }

    @Override
    public ArrayList<EnhancedMapTile> loadEnhancedMapTiles(){
        ArrayList<EnhancedMapTile> enhancedMapTiles = new ArrayList<>();
        return enhancedMapTiles;
    }

    @Override
    public ArrayList<NPC> loadNPCs(){
        ArrayList<NPC> npcs = new ArrayList<>();

        return npcs;
    }

    @Override
    public ArrayList<Trigger> loadTriggers() {
        ArrayList<Trigger> triggers = new ArrayList<>();
        return triggers;

    }

    @Override
    public void loadScripts() {

    }
    
}
