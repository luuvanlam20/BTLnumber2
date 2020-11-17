package Entities;

import Graphics.Sprite;

import java.util.List;

public class Brick extends Entity{

    public Brick(int xUnit, int yUnit) {
        super(xUnit, yUnit);
        this.time = 10;
        this.img = Sprite.brick.getFxImage();
        score = 10;
    }

    public void intoE(char[][]map, List<Entity> entities,int i) {
        char charW = map[y/32][x/32];
        String each = Character.toString(charW);
        Entity object ;
        object = new Item(x/32,y/32,each);
        entities.add(entities.size(),object);
    }
    @Override
    public void update() {
        if (!existEntity) {
            if (step == 0) img = Sprite.brick_exploded.getFxImage();
            else if (step == 1) img = Sprite.brick_exploded1.getFxImage();
            else if (step == 2) img = Sprite.brick_exploded2.getFxImage();
            if (aTime == time) step++;
            }
        if (aTime == time) aTime = 0;
        }
}
