package Level;

import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import java.util.HashMap;

// This class represents a map entity, which is any "entity" on a map besides the player
public abstract class MapEntity extends GameObject {
    protected MapEntityStatus mapEntityStatus = MapEntityStatus.ACTIVE;

    // if true, entity cannot go out of camera's update range
    protected boolean isUpdateOffScreen = false;

    // if true, entity will no longer be updated or drawn on the map
    protected boolean isHidden = false;

    protected String existenceFlag;
    protected Script interactScript;
    protected boolean isUncollidable = false;

    public MapEntity(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(spriteSheet, x, y, startingAnimation);
    }

    public MapEntity(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
    }

    public MapEntity(float x, float y, Frame[] frames) {
        super(x, y, frames);
    }

    public MapEntity(float x, float y, Frame frame) {
        super(x, y, frame);
    }

    public MapEntity(float x, float y) {
        super(x, y);
    }

    public MapEntityStatus getMapEntityStatus() {
        return mapEntityStatus;
    }

    public void setMapEntityStatus(MapEntityStatus mapEntityStatus) {
        this.mapEntityStatus = mapEntityStatus;
    }

    public boolean isUpdateOffScreen() {
        return isUpdateOffScreen;
    }

    public void setIsUpdateOffScreen(boolean isUpdateOffScreen) {
        this.isUpdateOffScreen = isUpdateOffScreen;
    }

    public Script getInteractScript() { return interactScript; }

    public void setInteractScript(Script interactScript) {
        this.interactScript = interactScript;
        this.interactScript.setMapEntity(this);
    }

    protected Script loadScript() {
        return null;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public boolean exists() {
        return this.existenceFlag == null || !map.getFlagManager().isFlagSet(this.existenceFlag);
    }

    public String getExistenceFlag() {
        return existenceFlag;
    }

    public void setExistenceFlag(String existenceFlag) {
        this.existenceFlag = existenceFlag;
    }

    public boolean isUncollidable() {
        return isUncollidable;
    }

    

    public void setIsUncollidable(boolean isUncollidable) {
        this.isUncollidable = isUncollidable;
    }

    // Abstract update method to be implemented by child classes (e.g., NPC)
    public abstract void update(Player player);
}
