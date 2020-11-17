package Entities;

import Graphics.Sprite;

public class Item extends Entity{
    String each;
    public Item(int xUnit, int yUnit,String each) {
        super(xUnit, yUnit);
        this.each = each;
        if (each.equals("b")) img = Sprite.powerup_bombs.getFxImage();
        else if (each.equals("f")) img = Sprite.powerup_flames.getFxImage();
        else if (each.equals("s")) img = Sprite.powerup_speed.getFxImage();
        else if (each.equals("x")) img = Sprite.portal.getFxImage();
        step = 0;
    }

    @Override
    public void update() {

    }
}
