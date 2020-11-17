package Entities;

import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Bomber extends Entity {
    protected int flameLong ;
    protected int bombSl ;
    protected int enemy;
    protected int hp;
    protected boolean deadAt;
    protected boolean isWon;
    protected int level;
    protected Image hpImg = Sprite.extra_life.getFxImage();
    public Bomber(int xUnit, int yUnit) {
        super(xUnit, yUnit);
        isWon = true;
        step = 0;
        score = 0;
        level =1;
        time = 15;
    }
    public void setPlayAgain() {
        img = Sprite.player_right.getFxImage();
        x = 32;
        y = 32;
        step = 0;
        score = 0;
        speed = 4;
        enemy = 0;
        time = 15;
        aTime=0;
        level =1;
        flameLong =1;
        hp = 1;
        deadAt = false;
        isWon = false;
        bombSl =1;
        existEntity = true;
    }
    public int getBombSl() {
        return bombSl;
    }


    public void setEnemy(int enemy) {
        this.enemy = enemy;
    }

    public int getEnemy() {
        return enemy;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public boolean isWon() {
        return isWon;
    }

    public void setWon(boolean won) {
        isWon = won;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void lostHp() {
        hp --;
        if (hp>0) {
            deadAt = true;
            aTime = 0;
        } else setDestroy();
    }
    public void checkBomb(List<Bomb> bombs) {
        if (!deadAt) {
            for (int i = 0; i < bombs.size(); i++)
                if (existEntity && bombs.get(i).bombRun.size() > 1 && bombs.get(i).getStep() > 2) {
                    for (int j = 0; j < bombs.get(i).bombRun.size(); j++)
                        if (comparePosTo(bombs.get(i).bombRun.get(j))) {
                            lostHp();
                            break;
                        }
                }
        }
    }
    public void checkItem(Item item) {
            if (item.each.equals("s") && speed ==4) {
                speed = 8;
                x = x/32 *32;
                y = y/32 *32;
            }
            if (item.each.equals("f")) flameLong += 1;
            if (item.each.equals("b")) bombSl += 1;
            if (item.each.equals("x") && enemy ==0) {
                level++;
                isWon = true;
            }
    }
    public void mapUpdate(List<Entity> entities,char[][]map,List<Bomb> bombs,List<Entity> stillObjects,int WIDTH) {
        for (int i =0; i<entities.size(); i++) {
            if (entities.get(i).getStep() > 2) {
                if (entities.get(i) instanceof Brick) {
                    ((Brick) entities.get(i)).intoE(map, entities, i);
                    }
                if (entities.get(i) instanceof Enemy) {
                    if (((Enemy) entities.get(i)).type == 3) {
                        Bomb object = new Bomb(entities.get(i).x/32,
                                entities.get(i).y/32);
                        object.setDestroy();
                        object.step = 3;
                        bombs.add(object);
                    }
                    enemy--;
                }
                score += entities.get(i).score;
                entities.remove(i);
            } else {
                if (entities.get(i) instanceof Enemy) {
                    if (entities.get(i).isExistEntity())
                        ((Enemy) entities.get(i)).Move(stillObjects, this, WIDTH);
                    if (comparePosTo(entities.get(i)) && existEntity && !deadAt &&entities.get(i).existEntity) lostHp();
                    ((Enemy) entities.get(i)).checkBomb(bombs);
                } else if (entities.get(i) instanceof Item) {
                    if (comparePosTo(entities.get(i))) {
                        checkItem((Item) entities.get(i));
                        if (!((Item) entities.get(i)).each.equals("x")) entities.remove(i);
                    }
                }
            }
        }
    }

    public void bombsExploded(List<Bomb> bombs,List<Entity> stillObjects,List<Entity> entities,int WIDTH) {
        if (!bombs.isEmpty()) {
            for (int i = 0; i < bombs.size(); i++) {
                if (bombs.get(i).getStep() > 2) {
                    if (bombs.get(i).isBombDone()) bombs.remove(i);
                    else if (bombs.get(i).checkBombRun())
                        bombs.get(i).BombE(stillObjects, entities, flameLong, WIDTH);
                }
            }
        }

    }
    @Override
    public void move(List<Entity> stillObjects,int WIDTH) {
        if (xSpeed <0) {
            if (x%32 !=0) x = x+xSpeed;
            else if (stillObjects.get(x / 32 - 1 + y / 32 * WIDTH) instanceof Grass) {
                if (y%32 == 0 || stillObjects.get(x / 32 - 1 + y / 32 * WIDTH + WIDTH) instanceof Grass)
                    x = x + xSpeed;
            }
        }
        if (xSpeed >0) {
            if (x%32 !=8) x = x+xSpeed;
            else if (stillObjects.get(x / 32 + 1 + y / 32 * WIDTH) instanceof Grass) {
                if (y%32 ==0 || stillObjects.get(x / 32 + 1 + y / 32 * WIDTH + WIDTH) instanceof Grass)
                    x = x + xSpeed;
            }
        }
        if (ySpeed <0) {
            if (y%32 !=0) y = y+ySpeed;
            else if (stillObjects.get(x / 32 - WIDTH+ y / 32 * WIDTH) instanceof Grass) {
                if (x%32 <=8 ||stillObjects.get(x / 32 - WIDTH + y / 32 * WIDTH +1) instanceof Grass)
                    y = y +ySpeed;
            }
        }
        if (ySpeed >0) {
            if (y%32 !=0) y = y +ySpeed;
            else if (stillObjects.get(x / 32 + WIDTH+ y / 32 * WIDTH) instanceof Grass) {
                if (x%32 <=8 || stillObjects.get(x / 32 + WIDTH + y / 32 * WIDTH +1) instanceof Grass)
                    y = y +ySpeed;
            }
        }

    }

    public void createObjectsList(int i, int j, char[][]map, List<Entity> stillObjects,List<Entity> entities) {
        String each = Character.toString(map[j][i]);
        Entity object;
        if (each.equals("p")) setPos(i,j);
        if (each.equals("#")) object = new Wall(i, j);
        else if (each.equals("*")) object = new Brick(i, j);
        else if (each.equals("1") || each.equals("2") || each.equals("6")
                || each.equals("3") || each.equals("4") || each.equals("5")) {
            object = new Enemy(i, j, each);
            setEnemy(getEnemy() + 1);
        } else object = new Grass(i, j);
        if (each.equals("#") || each.equals("*") || each.equals(" "))
            stillObjects.add(object);
        else if (each.equals("x") || each.equals("b")
                || each.equals("f") || each.equals("s")) {
            Entity newObject = new Brick(i, j);
            stillObjects.add(newObject);
        } else {
            entities.add(object);
            map[j][i] = ' ';
            Entity newObject = new Grass(i, j);
            stillObjects.add(newObject);
        }

        isWon = false;
    }
    @Override
    public boolean comparePosTo(Entity s2) {
        if (s2.x - x > 0) return ((s2.x -x <24) && Math.abs(s2.y - y) <Sprite.SCALED_SIZE);
         else return (s2.x - x >-Sprite.SCALED_SIZE && Math.abs(s2.y - y) <Sprite.SCALED_SIZE);
    }

    @Override
    public void update() {
        if (existEntity) {

            if (xSpeed >0) {
                if (step == 0) img = Sprite.player_right.getFxImage();
                else if (step == 1) img = Sprite.player_right_1.getFxImage();
                else img = Sprite.player_right_2.getFxImage();
            }

            if (xSpeed <0) {
                if (step == 0) img = Sprite.player_left.getFxImage();
                else if (step == 1) img = Sprite.player_left_1.getFxImage();
                else img = Sprite.player_left_2.getFxImage();
            }

            if (ySpeed >0) {
                if (step == 0) img = Sprite.player_down.getFxImage();
                else if (step == 1) img = Sprite.player_down_1.getFxImage();
                else img = Sprite.player_down_2.getFxImage();
            }

            if (ySpeed <0) {
                if (step == 0) img = Sprite.player_up.getFxImage();
                else if (step == 1) img = Sprite.player_up_1.getFxImage();
                else img = Sprite.player_up_2.getFxImage();
            }

        }else  {
            if (step == 0) img = Sprite.player_dead1.getFxImage();
            else if (step == 1) img = Sprite.player_dead2.getFxImage();
            else if (step == 2) img = Sprite.player_dead3.getFxImage();
            if (aTime%time ==0) step++;
        }
        if (deadAt) {
            if (aTime == time *5) deadAt = false;
            if (aTime < time/2)  img = Sprite.player_dead1.getFxImage();
        }
        if (aTime == time * 5) aTime = 0;
      setxSpeed(0);
      setySpeed(0);
    }

    public void render(GraphicsContext gc, int WIDth) {
        for (int i=1; i<=hp; i++) gc.drawImage(hpImg,Sprite.SCALED_SIZE *(WIDth-i),Sprite.SCALED_SIZE);
        if (deadAt) {
            if (aTime % 7 == 0) gc.drawImage(img, x, y + Sprite.SCALED_SIZE * 2);
        }else if (step < 3) gc.drawImage(img, x, y + Sprite.SCALED_SIZE * 2);
        aTime++;
    }
}
