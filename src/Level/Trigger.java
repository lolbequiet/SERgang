package Level;

import Engine.GraphicsHandler;
import GameObject.Rectangle;

import java.awt.*;

// This class represents a trigger script that can be placed on a map.
// Upon the player colliding with the trigger, it activates the attached script.
public class Trigger extends MapEntity {
    protected Script triggerScript;

    public Trigger(float x, float y, int width, int height, Script triggerScript) {
        super(x, y);
        this.triggerScript = triggerScript;
        this.setWidth(width);
        this.setHeight(height);
        this.setBounds(new Rectangle(0, 0, width, height));
    }

    public Trigger(float x, float y, int width, int height, Script triggerScript, String existenceFlag) {
        super(x, y);
        this.triggerScript = triggerScript;
        this.setWidth(width);
        this.setHeight(height);
        this.setBounds(new Rectangle(0, 0, width, height));
        this.existenceFlag = existenceFlag;
    }

    // Checks if the player collides with the trigger, then activates and updates the script.
    @Override
    public void update(Player player) {
        if (exists() && getBounds().intersects(player.getBounds())) {
            if (!triggerScript.isActive()) {
                triggerScript.setIsActive(true);  // Activate the script
                triggerScript.initialize();  // Initialize the script if needed
            }
            triggerScript.update();  // Execute the script logic
        }
    }

    // Draws the trigger bounds in red for debugging purposes.
    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        drawBounds(graphicsHandler, Color.RED);
    }

    // Optionally allows the bounds to be drawn in other colors.
    public void draw(GraphicsHandler graphicsHandler, Color color) {
        Rectangle scaledCalibratedBounds = getCalibratedBounds();
        scaledCalibratedBounds.setColor(color);
        scaledCalibratedBounds.setBorderColor(Color.BLACK);
        scaledCalibratedBounds.setBorderThickness(1);
        scaledCalibratedBounds.draw(graphicsHandler);
    }

    // Getter and setter methods for the script attached to the trigger.
    public Script getTriggerScript() {
        return triggerScript;
    }

    public void setTriggerScript(Script triggerScript) {
        this.triggerScript = triggerScript;
    }
}
