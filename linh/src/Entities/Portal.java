package Entities;

import Graphics.Sprite;

public class Portal extends Entity {
    public Portal(int xUnit, int yUnit) {
        super(xUnit, yUnit);
        this.img = Sprite.portal.getFxImage();
    }

    @Override
    public void update() {

    }
}
