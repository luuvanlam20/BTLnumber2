package Entities;

import Graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class Enemy extends Entity{

    protected int orientate =4;
    protected int hp =1;
    protected boolean bomb = false;
    protected int type = -1;
    private int[] randOri = new int[4];
    private int lengthRand;
    private int randomTime;
    private boolean atRight = true;


    /**
     * orientate :
     * 1. up
     * 2. right
     * 3. down
     * 4. left
     */
    public Enemy(int xUnit, int yUnit,String each) {
        super(xUnit, yUnit);
        time = 300;
        step = 0;
        if (each.equals("1")) {
            type = 1;
            speed = 2;
            score = 20;
        } else if (each.equals("2")) {
            type = 2;
            speed = 4;
            score = 30;
        } else if (each.equals("3")) {
            type = 3;
            speed = 4;
            score = 50;
        } else if (each.equals("4")) {
            type = 4;
            speed = 8;
            score = 70;
        } else if (each.equals("5")) {
            type  = 5;
            speed = 4;
            hp = 2;
            score = 100;
        } else if (each.equals("6")) {
            type = 6;
            speed = 4;
            score = 150;
            aTime = (int) (Math.random()  * time);
        }


    }
    public void checkBomb(List<Bomb> bombs) {
        if (!bomb) {
            for (int i = 0; i < bombs.size(); i++)
                if (existEntity && bombs.get(i).bombRun.size() > 1 && bombs.get(i).getStep() > 2) {
                    for (int j = 0; j < bombs.get(i).bombRun.size(); j++)
                        if (comparePosTo(bombs.get(i).bombRun.get(j)))
                        {
                            hp--;
                            if (hp >0) {
                                bomb = true;
                                aTime = 0;
                            } else
                                setDestroy();
                            break;
                        }
                }
        }
    }
    public int checkSeePlayer(List<Entity> stillObject,Bomber bomberman,int WIDTH) {
        if (bomberman.existEntity)
        if (x/32 == bomberman.x/32) {
            if (y/32 <= bomberman.y/32) {
                for (int i = y / 32; i < bomberman.y / 32; i++)
                    if (stillObject.get(x / 32 + i * WIDTH) instanceof Wall
                            || stillObject.get(x / 32 + i * WIDTH) instanceof Brick) return 0;
                return 3;
            }
            else {
                for (int i = bomberman.y / 32; i < y / 32; i++)
                    if (stillObject.get(x / 32 + i * WIDTH) instanceof Wall
                            || stillObject.get(x / 32 + i * WIDTH) instanceof Brick) return 0;
                return 1;
            }
        }
        else if (y/32 == bomberman.y/32) {
            if (x/32<=bomberman.x/32) {
                for (int i=x/32; i<bomberman.x/32;i++)
                    if (stillObject.get(y/32 *WIDTH +i) instanceof Wall
                    ||stillObject.get(y/32 *WIDTH +i) instanceof Brick) return 0;
                    return 2;
            } else {
                for (int i=bomberman.x/32; i<x/32;i++)
                    if (stillObject.get(y/32 *WIDTH +i) instanceof Wall
                            ||stillObject.get(y/32 *WIDTH +i) instanceof Brick) return 0;
                return 4;
            }
        }
        return 0;
    }
    public void Move(List<Entity> stillObject,Bomber bomberman,int WIDTH) {
        if (x % 32 == 0 && y % 32 == 0) {
            if (checkSeePlayer(stillObject, bomberman,WIDTH) == 0) {
                lengthRand = 0;
                if (stillObject.get(x / 32 - 1 + y / 32 * WIDTH) instanceof Grass) {
                    randOri[lengthRand] = 4;
                    lengthRand++;
                }
                if (stillObject.get(x / 32 + 1 + y / 32 * WIDTH) instanceof Grass) {
                    randOri[lengthRand] = 2;
                    lengthRand++;
                }
                if (stillObject.get(x / 32 + y / 32 * WIDTH - WIDTH) instanceof Grass) {
                    randOri[lengthRand] = 1;
                    lengthRand++;
                }
                if (stillObject.get(x / 32 + y / 32 * WIDTH + WIDTH) instanceof Grass) {
                    randOri[lengthRand] = 3;
                    lengthRand++;
                }
                lengthRand = (int) (Math.random() * lengthRand);
                if (lengthRand <4) lengthRand = randOri[lengthRand];
                orientate = lengthRand;

            } else orientate = checkSeePlayer(stillObject, bomberman,WIDTH);
        }
        switch (orientate) {
            case 1: ySpeed = -speed; xSpeed =0; break;
            case 2: ySpeed = 0; xSpeed = speed; atRight = true; break;
            case 3: ySpeed = speed; xSpeed = 0; break;
            case 4: ySpeed = 0; xSpeed = -speed; atRight = false; break;
            default: ySpeed =0; xSpeed =0; break;
        }
        if (aTime % 7 == 0) {
            autoStep();
            move(stillObject, WIDTH);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!existEntity) gc.drawImage(img, x, y+64);
        else {
            if (bomb) {
                if (aTime % 7 == 0) gc.drawImage(img, x, y+64);
            } else if (step < 3) {
                if (type == 6) {
                    if (aTime >= randomTime) gc.drawImage(img, x, y+64);
                } else gc.drawImage(img, x, y+64);
            }
        }
        aTime++;
    }

    @Override
    public void update() {

        if (existEntity) {
            switch (type) {
                case 1:
                    if (atRight) {
                        if (step == 0) img = Sprite.balloon_right1.getFxImage();
                        else if (step == 1) img = Sprite.balloon_right2.getFxImage();
                        else img = Sprite.balloon_right3.getFxImage();
                    } else  {
                        if (step == 0) img = Sprite.balloon_left1.getFxImage();
                        else if (step == 1) img = Sprite.balloon_left2.getFxImage();
                        else img = Sprite.balloon_left3.getFxImage();
                    }
                    break;
                case 2:
                    if (atRight) {
                        if (step == 0) img = Sprite.oneal_right1.getFxImage();
                        else if (step == 1) img = Sprite.oneal_right2.getFxImage();
                        else img = Sprite.oneal_right3.getFxImage();
                    }else {
                        if (step == 0) img = Sprite.oneal_left1.getFxImage();
                        else if (step == 1) img = Sprite.oneal_left2.getFxImage();
                        else img = Sprite.oneal_left3.getFxImage();
                    }
                    break;
                case 3:
                    if (atRight) {
                        if (step == 0) img = Sprite.doll_right1.getFxImage();
                        else if (step == 1) img = Sprite.doll_right2.getFxImage();
                        else img = Sprite.doll_right3.getFxImage();
                    }else {
                        if (step == 0) img = Sprite.doll_left1.getFxImage();
                        else if (step == 1) img = Sprite.doll_left2.getFxImage();
                        else img = Sprite.doll_left3.getFxImage();
                    }
                    break;
                case 4:
                    if (atRight) {
                        if (step == 0) img = Sprite.minvo_right1.getFxImage();
                        else if (step == 1) img = Sprite.minvo_right2.getFxImage();
                        else img = Sprite.minvo_right3.getFxImage();
                    }else {
                        if (step == 0) img = Sprite.minvo_left1.getFxImage();
                        else if (step == 1) img = Sprite.minvo_left2.getFxImage();
                        else img = Sprite.minvo_left3.getFxImage();
                    }
                    break;
                case 5:
                        if (atRight) {
                            if (step == 0) img = Sprite.kondoria_right1.getFxImage();
                            else if (step == 1) img = Sprite.kondoria_right2.getFxImage();
                            else img = Sprite.kondoria_right3.getFxImage();
                        }else  {
                            if (step == 0) img = Sprite.kondoria_left1.getFxImage();
                            else if (step == 1) img = Sprite.kondoria_left2.getFxImage();
                            else img = Sprite.kondoria_left3.getFxImage();
                        }
                    if (bomb) {
                        if (aTime == time/5) bomb = false;
                        if (aTime == time/10) img = Sprite.kondoria_dead.getFxImage();
                    }
                    break;
                case 6:
                    if (atRight) {
                        if (step == 0) img = Sprite.ghost_right1.getFxImage();
                        else if (step == 1) img = Sprite.ghost_right2.getFxImage();
                        else img = Sprite.ghost_right3.getFxImage();
                    }else {
                        if (step == 0) img = Sprite.ghost_left1.getFxImage();
                        else if (step == 1) img = Sprite.ghost_left2.getFxImage();
                        else img = Sprite.ghost_left3.getFxImage();
                    }
                    break;
                default:break;
            }
        } else {
            if (aTime<time/20) {
               switch (type) {
                case 1:
                    img = Sprite.balloon_dead.getFxImage();
                    break;
                case 2:
                    img = Sprite.oneal_dead.getFxImage();
                    break;
                case 3:
                    img = Sprite.doll_dead.getFxImage();
                    break;
                case 4:
                    img = Sprite.minvo_dead.getFxImage();
                    break;
                case 5:
                    img = Sprite.kondoria_dead.getFxImage();
                    break;
                case 6:
                    img = Sprite.ghost_dead.getFxImage();
                    break;
                default:
                    break;
              }
            } else {
                if (step == 0) img = Sprite.mob_dead1.getFxImage();
                else if (step == 1) img = Sprite.mob_dead2.getFxImage();
                else img = Sprite.mob_dead3.getFxImage();
                if (aTime % (time/20) ==0) step++;
            }
        }
      if (aTime == time) {
          aTime = 0;
          randomTime = (int) (Math.random()  * time);
      }
    }

}
