package Entities;

import Graphics.Sprite;

public class Wall extends Entity {


    public Wall(int xUnit, int yUnit) {
        super(xUnit, yUnit);
        this.img = Sprite.wall.getFxImage();
    }

    @Override
    public void update() {

    }
}
