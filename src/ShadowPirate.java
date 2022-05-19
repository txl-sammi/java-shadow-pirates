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
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image PIRATE_PROJECTILE = new Image("res/pirate/pirateProjectile.png");
    private final Image BLACK_PROJECTILE = new Image("res/blackbeard/blackbeardProjectile.png");
    private final static String LEVEL1_FILE = "res/level0.csv";
    private final static String START_MESSAGE = "PRESS SPACE TO START\nPRESS S TO ATTACK\nUSE ARROW KEYS TO FIND LADDER";
    private final static String END_MESSAGE = "GAME OVER";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";

    private final static int FONT_SIZE = 55;
    private final static int FONT_Y_POS = 402;
    private final Font FONT = new Font("res/wheaton.otf", FONT_SIZE);

    private final static int MAX_ARRAY_SIZE = 49;
    private final static Block[] blocks = new Block[MAX_ARRAY_SIZE];
    private final static Pirate[] pirates = new Pirate[MAX_ARRAY_SIZE];
    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    private Sailor sailor;
    private boolean gameOn;
    private boolean gameEnd;
    private boolean gameWin;

    int bottomEdge = 0;
    int topEdge = 0;
    int leftEdge = 0;
    int rightEdge = 0;

    public ShadowPirate(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV(LEVEL1_FILE);
        gameWin = false;
        gameEnd = false;
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
                    blocks[currentBlock] = new Block(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    currentBlock++;
                }
                if (sections[0].equals("Pirate")){
                    pirates[currentPirate] = new Pirate(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                    currentPirate++;
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

        if (!gameOn){
            drawStartScreen(input);
        }

        if (gameWin){
            drawEndScreen(WIN_MESSAGE);
        }

        // when game is running
        if (gameOn && !gameEnd && !gameWin){
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            for (Block block : blocks) {
                block.update();
            }
            sailor.update(input, blocks, pirates);
            for (Pirate pirate : pirates) {
                if (!(pirate == null)) {
                    pirate.update(sailor, blocks);
                    if (pirate.canShoot(sailor)){
                        System.out.println(pirate.directionFromSailor(sailor));
                        projectiles.add(pirate.shoot(sailor));
                    };
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
                gameEnd = true;
            }

            if (sailor.hasWon()){
                gameWin = true;
            }
        }
    }

    /**
     * Method used to draw the start screen instructions
     */
    private void drawStartScreen(Input input){
        FONT.drawString(START_MESSAGE, (Window.getWidth()/2.0 - (FONT.getWidth(START_MESSAGE)/2.0)),
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
}