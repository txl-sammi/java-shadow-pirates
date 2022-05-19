import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SWEN20003 Project 1, Semester 1, 2022
 *
 * @author Tharun Dharmawickrema
 */
public class ShadowPirate extends AbstractGame{
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";
    private final Image BACKGROUND1_IMAGE = new Image("res/background0.png");
    private final Image BACKGROUND2_IMAGE = new Image("res/background1.png");
    private final static String LEVEL0_FILE = "res/level0.csv";
    private final static String LEVEL1_FILE = "res/level1.csv";
    private final static String LVL1_START_MESSAGE = "PRESS SPACE TO START\nPRESS S TO ATTACK\nUSE ARROW KEYS TO FIND LADDER";
    private final static String LVL2_START_MESSAGE = "PRESS SPACE TO START\nPRESS S TO ATTACK\nFIND THE TREASURE";
    private final static String END_MESSAGE = "GAME OVER";
    private final static String LEVEL_COMPLETE_MESSAGE = "LEVEL COMPLETE!";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static int MESSAGE_DURATION = 3000;

    private final static int FONT_SIZE = 55;
    private final static int FONT_Y_POS = 402;
    private final Font FONT = new Font("res/wheaton.otf", FONT_SIZE);

    private final static int MAX_ARRAY_SIZE = 49;
    private final static Block[] blocks = new Block[MAX_ARRAY_SIZE];
    private final static Bomb[] bombs = new Bomb[MAX_ARRAY_SIZE];
    private final static Pirate[] pirates = new Pirate[MAX_ARRAY_SIZE];
    private final ArrayList<Projectile> projectiles = new ArrayList<>();

    private Sailor sailor;
    private Elixir elixir;
    private Sword sword;
    private Potion potion;
    private Treasure treasure;
    private boolean gameOn;
    private boolean levelEnd;
    private boolean levelWin;
    private boolean level0 = true;
    private boolean level1 = false;
    private boolean fileUnread = true;
    private int levelCompleteTime;
    private int bottomEdge = 0;
    private int topEdge = 0;
    private int leftEdge = 0;
    private int rightEdge = 0;

    public ShadowPirate(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV(LEVEL0_FILE);
        levelWin = false;
        levelEnd = false;
        gameOn = false;
    }

    /**
     * Entry point for program
     */
    public static void main(String[] args){
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            String line;
            if ((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                if (sections[0].equals("Sailor")){
                    sailor = new Sailor(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
            }

            int currentBlock = 0;
            int currentPirate = 0;
            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                if (sections[0].equals("Block")){
                    if (level0){
                        blocks[currentBlock] = new Block(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    } else{
                        blocks[currentBlock] = new Bomb(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    }
                    currentBlock++;
                }
                if (sections[0].equals("Pirate")){
                    pirates[currentPirate] = new Pirate(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    currentPirate++;
                }
                if (sections[0].equals("Blackbeard")){
                    pirates[currentPirate] = new Blackbeard(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    currentPirate++;
                }
                if (sections[0].equals("Treasure")){
                    treasure = new Treasure(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
                if (sections[0].equals("Elixir")){
                    elixir = new Elixir(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
                if (sections[0].equals("Potion")){
                    potion = new Potion(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
                if (sections[0].equals("Sword")){
                    sword = new Sword(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                }
                if (sections[0].equals("BottomRight")){
                    rightEdge = Integer.parseInt(sections[1]);
                    bottomEdge = Integer.parseInt(sections[2]);
                }
                if (sections[0].equals("TopLeft")) {
                    leftEdge = Integer.parseInt(sections[1]);
                    topEdge = Integer.parseInt(sections[2]);
                }
            }
            sailor.setBound(bottomEdge, topEdge, leftEdge, rightEdge);

            for (Pirate pirate : pirates) {
                if (!(pirate == null)) {
                    pirate.setBound(bottomEdge, topEdge, leftEdge, rightEdge);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Performs a state update. Pressing escape key,
     * allows game to exit.
     */
    @Override
    public void update(Input input){

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if (input.wasPressed(Keys.W)){
            level0 = false;
            level1 = true;
        }

        if (level0){
            if (!gameOn){
                drawStartScreen(input, LVL1_START_MESSAGE);
            }

            if (levelWin){
                int now = (int) System.currentTimeMillis();
                if (!((levelCompleteTime + MESSAGE_DURATION <= now))){
                    drawEndScreen(LEVEL_COMPLETE_MESSAGE);
                } else {
                    level0 = false;
                    levelWin = false;
                    gameOn = false;
                    level1 = true;
                }
            }

            if (levelEnd){
                drawEndScreen(END_MESSAGE);
            }

            // when game is running
            if (gameOn && !levelEnd && !levelWin && level0){
                BACKGROUND1_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
                for (Block block : blocks) {

                    block.update();
                }
                sailor.update(input, blocks, pirates);
                for (Pirate pirate : pirates) {
                    if (!(pirate == null)) {
                        pirate.update(blocks);
                        if (pirate.canShoot(sailor)){
                            projectiles.add(pirate.shoot(sailor));
                        }
                    }
                }

                for (Projectile projectile : projectiles){
                    if (!(projectile == null)){
                        if (!projectile.hasDisappeared()){
                            projectile.setBound(bottomEdge, topEdge, leftEdge, rightEdge);
                            projectile.update(sailor);
                        }
                    }
                }

                if (sailor.isDead()){
                    levelEnd = true;
                }

                if (sailor.hasWon()){
                    levelCompleteTime = (int) System.currentTimeMillis();
                    levelWin = true;

                    for (Projectile projectile : projectiles){
                        if (!(projectile == null)){
                            projectile.clear();
                        }
                    }
                }
            }
        }

        if (level1){
            if (!gameOn){
                drawStartScreen(input, LVL2_START_MESSAGE);
            }
            if (levelWin){
                drawEndScreen(WIN_MESSAGE);
            }

            if (levelEnd){
                drawEndScreen(END_MESSAGE);
            }

            if (gameOn && !levelEnd && !levelWin){
                BACKGROUND2_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
                if (fileUnread) {
                    clearLevel();
                    readCSV(LEVEL1_FILE);
                    fileUnread = false;
                }

                for (Block bomb : blocks) {
                    if (!(bomb == null)){
                        bomb.update();
                    }
                }

                sailor.update(input, blocks, pirates);
                elixir.update(sailor);
                potion.update(sailor);
                sword.update(sailor);
                treasure.update(sailor);

                for (Pirate pirate : pirates) {
                    if (!(pirate == null)) {
                        pirate.update(blocks);
                        if (pirate.canShoot(sailor)){
                            projectiles.add(pirate.shoot(sailor));
                        }
                    }
                }

                for (Projectile projectile : projectiles){
                    if (!(projectile == null)){
                        if (!projectile.hasDisappeared()){
                            projectile.setBound(bottomEdge, topEdge, leftEdge, rightEdge);
                            projectile.update(sailor);
                        }
                    }
                }

                if (sailor.isDead()){
                    levelEnd = true;
                }

                if (sailor.hasWon()){
                    levelWin = true;
                }
            }
        }
    }

    /**
     * Method used to draw the start screen instructions
     */
    private void drawStartScreen(Input input, String message){
        FONT.drawString(message, (Window.getWidth()/2.0 - (FONT.getWidth(message)/2.0)),
                FONT_Y_POS);
        if (input.wasPressed(Keys.SPACE)){
            gameOn = true;
        }
    }

    /**
     * Method used to draw end screen messages
     */
    private void drawEndScreen(String message){
        FONT.drawString(message, (Window.getWidth()/2.0 - (FONT.getWidth(message)/2.0)), FONT_Y_POS);
    }

    private void clearLevel(){
        for (int i=0; i < MAX_ARRAY_SIZE; i++) {
            blocks[i] = null;
            pirates[i] = null;
        }
        projectiles.removeIf(projectile -> !(projectile == null));
    }

}