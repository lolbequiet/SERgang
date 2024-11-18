package Level;

import Builders.FrameBuilder;
import GameObject.Frame;
import GameObject.GameObject;
import GameObject.SpriteSheet;
import Utils.Direction;
import Utils.Point;

public class Projectile extends GameObject {

    protected Point direction;
    protected float speed;
    protected float damage;

    public Projectile(float x, float y, Frame frame, Point direction, float speed, float damage) {
        super(x, y, frame);
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
    }

    @Override
    public void update() {
        moveRight(direction.x * speed);
        moveDown(direction.y * speed);

        super.update();
    }
}
