package Entities;

import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Entity{
    List<RunBomb> bombRun = new ArrayList<>();
    boolean BombDone = false;
    public Bomb(int xUnit, int yUnit) {
        super(xUnit, yUnit);
        this.time = 50;
        this.img = Sprite.bomb.getFxImage();
        speed = 10;
    }

    public boolean isBombDone() {
        return BombDone;
    }
    public boolean checkBombRun() {
        return bombRun.isEmpty();
    }
    public void BombE(List<Entity> stillObjects, List<Entity> entities, int longBomb,int WIDTH) {
        int preX = x/32;
        int preY = y/32;
        RunBomb centerE = new RunBomb(preX,preY,0);
        bombRun.clear();
        bombRun.add(centerE);
        for (int i=1; i<=longBomb; i++) {
            if (stillObjects.get(preX + (preY-i) * WIDTH) instanceof Grass) {
                RunBomb up;
                if (i<longBomb)  up = new RunBomb(preX, preY-i,1);
                else up = new RunBomb(preX, preY-i,3);
                bombRun.add(up);
            }
            else if (stillObjects.get(preX + (preY-i) * WIDTH) instanceof Brick){
                Entity up = new Grass(preX, preY-i);
                stillObjects.get(preX + (preY-i)*WIDTH).setDestroy();
                entities.add(stillObjects.get(preX + (preY-i)*WIDTH));
                stillObjects.set(preX + (preY-i) * WIDTH,up);
                break;
            }
            else break;
        }
        for (int i=1; i<=longBomb; i++) {
            if (stillObjects.get(preX + (preY+i) * WIDTH) instanceof Grass) {
                RunBomb down;
                if (i<longBomb)  down = new RunBomb(preX, preY+i,1);
                else down = new RunBomb(preX, preY+i,4);
                bombRun.add(down);
            }
            else if (stillObjects.get(preX + (preY+i) * WIDTH) instanceof Brick){
                Entity newE= new Grass(preX, preY+i);
                stillObjects.get(preX+ (preY+i)*WIDTH).setDestroy();
                entities.add( stillObjects.get(preX+ (preY+i)*WIDTH));
                stillObjects.set(preX + (preY+i) * WIDTH,newE);
                break;
            } else break;
        }
        for (int i=1; i<=longBomb; i++) {
            if (stillObjects.get(preX-i + preY * WIDTH) instanceof Grass) {
                RunBomb left;
                if (i<longBomb)  left = new RunBomb(preX -i, preY,2);
                else left = new RunBomb(preX-i, preY,5);
                bombRun.add(left);
            }
            else if (stillObjects.get(preX-i + preY * WIDTH) instanceof Brick){
                Entity newE = new Grass(preX-i, preY);
                stillObjects.get(preX-i + preY*WIDTH).setDestroy();
                entities.add( stillObjects.get(preX-i + preY*WIDTH));
                stillObjects.set(preX-i + preY * WIDTH,newE);
                break;
            }else break;
        }
        for (int i=1; i<=longBomb; i++) {
            if (stillObjects.get(preX+i + preY * WIDTH) instanceof Grass) {
                RunBomb right;
                if (i<longBomb)  right = new RunBomb(preX+i,preY,2);
                else right = new RunBomb(preX+i, preY,6);
                bombRun.add(right);
            }
            else if (stillObjects.get(preX+i + preY * WIDTH) instanceof Brick){
                Entity newE = new Grass(preX+i, preY);
                stillObjects.get(preX+i + preY*WIDTH).setDestroy();
                entities.add( stillObjects.get(preX+i + preY*WIDTH));
                stillObjects.set(preX+i + preY * WIDTH,newE);
                break;
            }else break;
        }
    }
    @Override
    public void render(GraphicsContext gc) {
        if (step <3 ) {
            if (aTime % 3 == 0) gc.drawImage(img, x, y+64);
        }
        else bombRun.forEach(g -> g.render(gc));
        aTime++;
    }

    @Override
    public void update() {
        if (!bombRun.isEmpty()) {
            bombRun.forEach(RunBomb::update);
            if (bombRun.get(0).getStep() > 2) BombDone = true;
        }
        if (step == 0) img = Sprite.bomb.getFxImage();
        else if (step == 1) img = Sprite.bomb_1.getFxImage();
        else if (step == 2) img = Sprite.bomb_2.getFxImage();
        if (aTime == time) {
            step++;
            aTime=0;
        }
    }


}
