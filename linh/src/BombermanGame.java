import Entities.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import Graphics.Sprite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class BombermanGame extends Application {
    
    public static int WIDTH = 31;
    public static int HEIGHT = 13;
    
    private GraphicsContext gc;
    private Canvas canvas;
    private Label Level = new Label("Level :0");
    private Label Score = new Label("Score :0");
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    private List<Bomb> bombs = new ArrayList<>();
    private Bomber bomberman = new Bomber(1,1);
    public char[][] map = new char[HEIGHT][WIDTH];

    String mapTxt ;
    FileReader fileReader;
    BufferedReader bRead;
    private boolean gameStart = false;
    private  AnimationTimer timer;
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, 64+Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();
        //set Label
        Font font = Font.font("Verdana", FontPosture.REGULAR,30);
        Level.setFont(Font.font(15));
        Level.setLayoutY(40);

        Score.setTextFill(Color.GOLDENROD);
        Score.setFont(font);
        Score.setLayoutX(400);
        Score.setLayoutY(10);
        // Tao root container
        Group root = new Group();
        root.isAutoSizeChildren();
        root.getChildren().add(canvas);
        root.getChildren().add(Level);
        root.getChildren().add(Score);
        root.setAutoSizeChildren(true);

        // Tao scene
        Scene scene = new Scene(root);
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                if (gameStart) {
                    if (bomberman.isWon()) {
                        scene.setRoot(root);
                        System.out.println(stage.getWidth() + " " + stage.getHeight());
                        System.out.println(canvas.getWidth() + " " + canvas.getHeight());
                        try {
                            mapTxt =new File("").getAbsolutePath() + "/src/Graphics/res/levels/Level" + bomberman.getLevel() +".txt";
                            fileReader = new FileReader(mapTxt);
                        } catch (IOException e) {
                            System.out.println("File not found!!!");
                        }
                        bRead = new BufferedReader(fileReader);
                        createMap();
                        stage.setMaxWidth(Sprite.SCALED_SIZE * WIDTH +16);
                        stage.setMaxHeight(64+Sprite.SCALED_SIZE * HEIGHT  +39);
                        //stage.setFullScreen(true);

                    }
                    if (bomberman.getStep() >2) {
                        gameStart = false;
                        try {
                            fileReader.close();
                            bRead.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    render();
                    update();
                } else {
                    timer.stop();
                    bomberman.setPlayAgain();
                    bomberman.setWon(true);
                    scene.setRoot(menuRoot());
                }

            }
        };

       scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
           @Override
           public void handle(KeyEvent event) {
               if (gameStart) {
                   if (bomberman.isExistEntity()) {
                       if (bomberman.getStep() == 0) bomberman.setStep(1);
                       else if (bomberman.getStep() == 1) bomberman.setStep(2);
                       else if (bomberman.getStep() == 2) bomberman.setStep(0);
                       switch (event.getCode()) {
                           case W:
                               bomberman.setySpeed(-bomberman.getSpeed());
                               break;
                           case S:
                               bomberman.setySpeed(bomberman.getSpeed());
                               break;
                           case A:
                               bomberman.setxSpeed(-bomberman.getSpeed());
                               break;
                           case D:
                               bomberman.setxSpeed(bomberman.getSpeed());
                               break;
                           case SPACE:
                               if (bombs.size() < bomberman.getBombSl()) {
                                   Bomb object = new Bomb(bomberman.getX() / 32,
                                           bomberman.getY() / 32);
                                   object.setDestroy();
                                   bombs.add(object);
                               }
                               break;
                           default:
                               break;
                       }
                   }
                   bomberman.move(stillObjects, WIDTH);
               }
           }
       });
       scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
           @Override
           public void handle(MouseEvent event) {
               if (event.isPopupTrigger() && bombs.size()<bomberman.getBombSl()
               && bomberman.isExistEntity() && gameStart) {
                   Bomb object = new Bomb(bomberman.getX()/32,
                           bomberman.getY()/32);
                   object.setDestroy();
                   bombs.add(object);
               }
           }
       });
        timer.start();
        stage.setScene(scene);
        stage.show();

    }

    public void createMap() {
        stillObjects.removeAll(stillObjects);
        entities.removeAll(entities);
        try {
            String line;
            if (!(line = bRead.readLine()).equals("")) {
                String[] data = line.split(" ");
                    WIDTH = Integer.parseInt(data[0]);
                    HEIGHT = Integer.parseInt(data[data.length -1]);
                for (int j = 0; j < HEIGHT; j++) {
                    if ((line = bRead.readLine()) != null) map[j] = line.toCharArray();
                    //System.out.println(line);
                    for (int i = 0; i < WIDTH; i++) {
                        bomberman.createObjectsList(i, j, map, stillObjects, entities);
                    }
                }
            } else {
                System.out.println("WIN !!!!!");
                gameStart = false;
                bRead.close();
            }
        } catch (IOException e) {
           System.out.println("ERROR");
        }
    }
    public void update() {
        entities.forEach(Entity::update);
        bomberman.update();
        bombs.forEach(Bomb::update);
        bomberman.bombsExploded(bombs,stillObjects,entities, WIDTH);
        bomberman.mapUpdate(entities,map,bombs,stillObjects,WIDTH);
        bomberman.checkBomb(bombs);
        Level.setText("Level : " + bomberman.getLevel());
        Score.setText("Score : " + bomberman.getScore());
    }
    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bombs.forEach(g -> g.render(gc));
        bomberman.render(gc,WIDTH);
    }

    public Group menuRoot() {
        Group root = new Group();
        String background;
        background =new File("").getAbsolutePath() + "/src/Graphics/res/item/";
        Image image=new Image("file:C:\\Users\\DELL\\IdeaProjects\\linh\\src\\Graphics\\res\\item\\menu.png");

        ImageView iv=new ImageView();
        iv.setImage(image);
        Button startButton = new Button("Play Game");
        startButton.setLayoutX(150);
        startButton.setLayoutY(150);
        Button HowToPlay =new Button("How To Play");
        HowToPlay.setLayoutX(150);
        HowToPlay.setLayoutY(200);
        Button Exit=new Button("Exit");
        Exit.setLayoutX(150);
        Exit.setLayoutY(250);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gameStart = true;
                System.out.println("Khoi tao");
                timer.start();
            }
        });
        HowToPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().clear();
                Button Ex= new Button("Back");
                Ex.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        root.getChildren().clear();
                        root.getChildren().add(iv);
                        root.getChildren().add(startButton);
                        root.getChildren().add(HowToPlay);
                        root.getChildren().add(Exit);
                    }
                });
                Ex.setLayoutX(930);
                Ex.setLayoutY(450);
                Label How= new Label("1/ Dòng đầu tiên bao gồm 3 số nguyên L, R, C:\n" +
                        "L - số thứ tự màn chơi\n" +
                        "R - số hàng của bản đồ\n" +
                        "C - số cột của bản đồ\n" +
                        "\n" +
                        "2/ R dòng tiếp theo, mỗi dòng có C kí tự. Mỗi kí tự đại diện cho một đối tượng trên bản đồ:\n" +
                        "Tiles:\n" +
                        "# - Wall\n" +
                        "* - Brick\n" +
                        "x - Portal\n" +
                        "\n" +
                        "Character:\n" +
                        "p - Bomber\n" +
                        "1 - Balloon\n" +
                        "2 - Oneal\n" +
                        "\n" +
                        "Items:\n" +
                        "b - Bomb Item\n" +
                        "f - Flame Item\n" +
                        "s - Speed Item\n" +
                        "\n" +
                        "Kí tự khác các kí tự trên - Grass\n"+
                        "Dùng A,S,W,D để di chuyển và cách để đặt boom.");
                root.getChildren().add(How);
                root.getChildren().add(Ex);
            }
        });
        Exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        root.getChildren().add(iv);
        root.getChildren().add(startButton);
        root.getChildren().add(HowToPlay);
        root.getChildren().add(Exit);
        return root;
    }
}
