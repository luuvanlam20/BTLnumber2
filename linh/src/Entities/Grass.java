package Entities;

import Graphics.Sprite;
import javafx.scene.image.Image;

public class Grass extends Entity {


    public Grass(int xUnit, int yUnit) {
        super(xUnit, yUnit);
        this.img = Sprite.grass.getFxImage();
    }

    @Override
    public void update() {

    }
}
