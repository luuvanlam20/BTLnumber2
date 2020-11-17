package Entities;

import Graphics.Sprite;

public class RunBomb extends Entity{
    protected int locate;
    /**locate:
     * 0 center
     * 1 hoz
     * 2 ver
     * 3 lastUp
     * 4 lastDown
     * 5 lastLeft
     * 6 lastRight
     */

    public RunBomb(int xUnit, int yUnit, int locate) {
        super(xUnit, yUnit);
        time = 7;
        this.locate = locate;
        existEntity = false;
        step = 0;
    }



    @Override
    public void update() {
        if (locate == 0) {
            if (step == 0) img = Sprite.bomb_exploded.getFxImage();
            else if (step == 1) img = Sprite.bomb_exploded1.getFxImage();
            else if (step == 2) img = Sprite.bomb_exploded2.getFxImage();
        }
        if (locate == 2) {
            if (step == 0) img = Sprite.explosion_horizontal.getFxImage();
            else if (step == 1) img = Sprite.explosion_horizontal1.getFxImage();
            else if (step == 2) img = Sprite.explosion_horizontal2.getFxImage();
        }
        if (locate == 1) {
            if (step == 0) img = Sprite.explosion_vertical.getFxImage();
            else if (step == 1) img = Sprite.explosion_vertical1.getFxImage();
            else if (step == 2) img = Sprite.explosion_vertical2.getFxImage();
        }
        if (locate == 3) {
            if (step == 0) img = Sprite.explosion_vertical_top_last.getFxImage();
            else if (step == 1) img = Sprite.explosion_vertical_top_last1.getFxImage();
            else if (step == 2) img = Sprite.explosion_vertical_top_last2.getFxImage();
        }
        if (locate == 4) {
            if (step == 0) img = Sprite.explosion_vertical_down_last.getFxImage();
            else if (step == 1) img = Sprite.explosion_vertical_down_last1.getFxImage();
            else if (step == 2) img = Sprite.explosion_vertical_down_last2.getFxImage();
        }
        if (locate == 5) {
            if (step == 0) img = Sprite.explosion_horizontal_left_last.getFxImage();
            else if (step == 1) img = Sprite.explosion_horizontal_left_last1.getFxImage();
            else if (step == 2) img = Sprite.explosion_horizontal_left_last2.getFxImage();
        }
        if (locate == 6) {
            if (step == 0) img = Sprite.explosion_horizontal_right_last.getFxImage();
            else if (step == 1) img = Sprite.explosion_horizontal_right_last1.getFxImage();
            else if (step == 2) img = Sprite.explosion_horizontal_right_last2.getFxImage();
        }

        if (aTime == time) {
            step++;
            aTime=0;
        }
    }
}
