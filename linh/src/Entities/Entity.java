package Entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import Graphics.Sprite;

import java.util.List;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected int x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected int y;
    protected boolean existEntity;
    protected int score = 0;
    protected int xSpeed;
    protected int ySpeed;
    protected int speed = 4;
    protected int step = 0;
    protected Image img;
    protected int time;
    protected int aTime = 0;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity( int xUnit, int yUnit, Image img) {
        existEntity = true;
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }
    public  Entity(int xUnit, int yUnit) {
        existEntity = true;
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
    }
    public void setPos(int xUnit, int yUnit) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
    }
    public void setStep(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public void autoStep() {
        step++;
        if (step == 3) step = 0;
    }

    public void setDestroy() {
        existEntity = false;
        step = 0;
        aTime = 0;
    }
    public boolean isExistEntity() {
        return existEntity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void move(List<Entity> stillObjects, int WIDTH) {
        int preY = y+ySpeed;
        int preX = x+xSpeed;
        if (stillObjects.get(preX/32 + preY/32 * WIDTH) instanceof Grass) {
            if (ySpeed <0) {
                if (x % 32 ==0) y = preY;
            }
            if (xSpeed <0)
                if (y%32 ==0) x = preX;
        }
        if (ySpeed >0) {
            if (stillObjects.get(x/32 + (y/32 +1) * WIDTH) instanceof Grass)
                if (x%32 ==0) y = preY;
        }
        if (xSpeed >0) {
                if (stillObjects.get(x/32 + 1 + y/32 * WIDTH) instanceof Grass) {
                    if (y % 32 == 0) x = preX;
                }
            }
    }
    public boolean comparePosTo(Entity s2){
        return (Math.abs(x - s2.x) <32 && Math.abs(y - s2.y) <32);
    }

    public void render(GraphicsContext gc) {
            if (step <3) gc.drawImage(img, x, y+64);
            aTime++;
    }
    public abstract void update();
}
