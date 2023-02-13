package snake;

import mosaic.MosaicCanvas;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;



public class SnakeGame {

    public enum GameStatus {RUNNING, PAUSED, WON, LOST};
    // fields 
    private MosaicCanvas board;
    private Snake snake;
    private FoodStuffs foodStuffs;
    private FoodStuffs badFoodStuffs; // level 4 and up
    private AnimationTimer animator;

    private MediaPlayer musicPlayer;
    private MediaPlayer winThemeShortPlayer;
    private MediaPlayer winThemeLongPlayer;
    private MediaPlayer gameOverMusicPlayer;

    private String splashScreenMessage;
    private int level;
    private int livesRemaining;

    private int gameSpeed;
    private boolean isRunning;
    private boolean isGameOver;
    public GameStatus gameStatus;
    private int framesPerUpdate;

    private Object boardColorData;//used for storing sqaure colors and 
                                  //restoring when play is started. Necessary
                                  //to "remove" on screen-text, such as from 
                                  //splash screen and pause screen

    //private static final AudioClip chompSound = new AudioClip(new File("./audio/chomp.wav").toURI().toURL().toString());
    //private static final AudioClip deathSound = new AudioClip(new File("./audio/pacmandeathsound.wav").toURI().toString());
    private static final AudioClip chompSound = new AudioClip(SnakeGame.class.getResource("resources/audio/chomp.wav").toString());
    private static final AudioClip deathSound = new AudioClip(SnakeGame.class.getResource("resources/audio/pacmandeathsound.wav").toString());
    /*
    private static final Media music = 
        new Media(new File("audio/music.wav").toURI().toString());
    private static final Media winThemeShort = 
        new Media(new File("audio/wintheme.mp3").toURI().toString());
    private static final Media winThemeLong = 
        new Media(new File("audio/winthemelong.mp3").toURI().toString());
    private static final Media gameOverMusic = 
        new Media(new File("audio/gameOverMusic.mp3").toURI().toString());
    */
    private static final Media music = 
        new Media(SnakeGame.class.getResource("resources/audio/music.wav").toString());
    private static final Media music3 = 
        new Media(SnakeGame.class.getResource("resources/audio/music3.mp3").toString());
    private static final Media music4 = 
        new Media(SnakeGame.class.getResource("resources/audio/music4.mp3").toString());
    private static final Media music5 = 
        new Media(SnakeGame.class.getResource("resources/audio/music5.mp3").toString());
    private static final Media winThemeShort = 
        new Media(SnakeGame.class.getResource("resources/audio/wintheme.mp3").toString());
    private static final Media winThemeLong = 
        new Media(SnakeGame.class.getResource("resources/audio/winthemelong.mp3").toString());
    private static final Media gameOverMusic = 
        new Media(SnakeGame.class.getResource("resources/audio/gameovermusic.mp3").toString());

    /*
    private static final Image coolSnake = new Image("./images/snakeCool.png");
    private static final Image deadSnake = new Image("./images/snakeDead.png");
    */
    /*
    private static final Image coolSnake = new Image(new File("./images/snakeCool.png").toURI().toString());
    private static final Image deadSnake = new Image(new File("./images/snakeDead.png").toURI().toString());
    */
    private static final Image coolSnake = new Image(SnakeGame.class.getResource("resources/images/snakecool.png").toString());
    private static final Image deadSnake = new Image(SnakeGame.class.getResource("resources/images/snakedead.png").toString());
    // same buttons defined in Main.java
    // used to enable/disable when game won or lost
    private Button startButton;
    private Button pauseButton;
    private Button nextLevelButton;
    private Button retryButton;


    private final static int ROWS = 40; // rows in the mosaic
    private final static int COLUMNS = 40; 
    private final static int SQUARE_SIZE = 15;// size of each square
    private final static Color BOARD_COLOR = Color.DARKGRAY;
    private final static Color SNAKE_COLOR = Color.GREEN;
    private final static Color SNAKE_HEAD_COLOR = Color.DARKGREEN;
    private final static Color FOOD_COLOR = Color.YELLOW;
    private final static Color BAD_FOOD_COLOR = Color.RED;
    private final static int MAX_LEVEL = 5;

    public SnakeGame(int level, int lives, HashMap<String,Button> buttonMap) {
        board = new MosaicCanvas(ROWS, COLUMNS, SQUARE_SIZE, SQUARE_SIZE);
        this.level = (level > 0) ? level : 1;
        this.livesRemaining = lives;

        board.fill(BOARD_COLOR);
        board.setGroutingColor(null);

        this.startButton = buttonMap.get("start");
        this.pauseButton = buttonMap.get("pause");
        this.nextLevelButton = buttonMap.get("nextLevel");
        this.retryButton = buttonMap.get("retry");

        // set music
        if (level == 5) 
            musicPlayer = new MediaPlayer(music5);
        else if (level == 4)
            musicPlayer = new MediaPlayer(music4);
        else if (level == 3)
            musicPlayer = new MediaPlayer(music3);
        else
            musicPlayer = new MediaPlayer(music);

        musicPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                musicPlayer.seek(new Duration(0));
                musicPlayer.play();
            }
        });

        winThemeShortPlayer = new MediaPlayer(winThemeShort);
        winThemeShortPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                winThemeShortPlayer.stop();
                winThemeShortPlayer.dispose();
            }
        });

        snake = new Snake(COLUMNS/2, ROWS/2, Snake.Direction.UP, COLUMNS, ROWS);

        foodStuffs = new FoodStuffs(COLUMNS, ROWS, 5*level, snake, null);

        if (level >= 3) {
            badFoodStuffs = new FoodStuffs(COLUMNS, ROWS, 3*(level-2), 
                                    snake, foodStuffs);
        }

        //gameSpeed = 8;// squares moved per second
        gameSpeed = switch (level) {
            case 1 -> 8;
            case 2 -> 10;
            case 3 -> 15;
            case 4 -> 20;
            case 5 -> 20;
            default -> 20;
        };
        framesPerUpdate = MAX_LEVEL + 1 - level;

        setBoardColors();// must occur after creating snake
                         // and foodStuffs

        setSplashScreenText();
        gameStatus = GameStatus.PAUSED; // initially paused

        animator = new AnimationTimer(){
            long prevFrameTime;
            long frameNumber; 

            public void handle(long time) {

                if (time - prevFrameTime > 0.99e9/gameSpeed) {
                    updateForNewFrame();
                    prevFrameTime = time;
                }
            }
        };

    }

    public MosaicCanvas getBoard() {
        return board;
    }

    /**
     * Update colors for snake coordinates */
    public void setBoardColors() {
        board.fill(BOARD_COLOR);

        // set food colors
        setFoodColors();
        if (badFoodStuffs != null)
            setBadFoodColors();

        // set snake colors
        int[] xs = snake.getXdirections();
        int[] ys = snake.getYdirections();

        board.setColor(ys[0], xs[0], SNAKE_HEAD_COLOR);

        for (int i = 1; i < snake.getSize(); i++) {
            board.setColor(ys[i], xs[i], SNAKE_COLOR);
        }
    }

    public void setFoodColors() {
        GraphicsContext g = board.getGraphicsContext2D();
        g.setFont(new Font(15));
        g.setTextAlign(TextAlignment.CENTER);
        for (FoodStuffs.FoodNode node : foodStuffs.getFoodList()) {
            board.setColor(node.y, node.x, FOOD_COLOR);
            String num = "" + node.value;
            g.setFill(Color.BLACK);
            g.fillText(num, node.x*SQUARE_SIZE + SQUARE_SIZE*0.5, node.y*SQUARE_SIZE+SQUARE_SIZE*0.8); 
        }
    }

    public void setBadFoodColors() {
        if (badFoodStuffs == null)
            return;
        GraphicsContext g = board.getGraphicsContext2D();
        g.setFont(new Font(15));
        g.setTextAlign(TextAlignment.CENTER);
        for (FoodStuffs.FoodNode node : badFoodStuffs.getFoodList()) {
            board.setColor(node.y, node.x, BAD_FOOD_COLOR);
        }
    
    }

    public void keyHandle(KeyEvent evt) {
        KeyCode code = evt.getCode();

        Snake.SnakeNode oldTail = snake.getTail();
        Snake.SnakeNode oldHead = snake.getHead();
        int oldTailX = oldTail.getX();
        int oldTailY = oldTail.getY();
        int oldHeadX = oldHead.getX();
        int oldHeadY = oldHead.getY();

        boolean moved = switch (code) {
            case UP -> snake.keyMove(Snake.Direction.UP);
            case DOWN -> snake.keyMove(Snake.Direction.DOWN);
            case LEFT -> snake.keyMove(Snake.Direction.LEFT);
            case RIGHT -> snake.keyMove(Snake.Direction.RIGHT);
            case G -> snake.grow();
            default -> false;
        };
        if (code != KeyCode.G && moved) {
            Snake.SnakeNode newHead = snake.getHead();
            board.setColor(newHead.getY(), newHead.getX(), SNAKE_HEAD_COLOR);
            board.setColor(oldHeadY, oldHeadX, SNAKE_COLOR); // snake body color
            board.setColor(oldTailY, oldTailX, BOARD_COLOR);
        }
        //setSnakeColors();
    }

    public void keyTurn(KeyEvent evt) {

        if (gameStatus != GameStatus.RUNNING)
            return;
        
        KeyCode code = evt.getCode();
        Snake.Direction currentDirection = snake.getDirection();

        Snake.SnakeNode oldHead = snake.getHead();
        int oldHeadX = oldHead.getX();
        int oldHeadY = oldHead.getY();
        switch (code) {
            case UP -> {
                if ((currentDirection == Snake.Direction.LEFT
                    || currentDirection == Snake.Direction.RIGHT)
                        && oldHeadY != 0)
                    snake.setSnakeDirection(Snake.Direction.UP);
            }
            case DOWN -> {
                if ((currentDirection == Snake.Direction.LEFT
                    || currentDirection == Snake.Direction.RIGHT)
                        && oldHeadY != ROWS-1)
                    snake.setSnakeDirection(Snake.Direction.DOWN);
            }
            case LEFT -> {
                if ((currentDirection == Snake.Direction.UP
                    || currentDirection == Snake.Direction.DOWN)
                        && oldHeadX != 0)
                    snake.setSnakeDirection(Snake.Direction.LEFT);
            }
            case RIGHT -> {
                if ((currentDirection == Snake.Direction.UP
                    || currentDirection == Snake.Direction.DOWN)
                        && oldHeadX != COLUMNS-1)
                    snake.setSnakeDirection(Snake.Direction.RIGHT);
            }
        }
    }

    public void updateForNewFrame() {
        // save tail position of snake
        Snake.SnakeNode oldTail = snake.getTail();
        Snake.SnakeNode oldHead = snake.getHead();
        int oldTailX = oldTail.getX();
        int oldTailY = oldTail.getY();
        int oldHeadX = oldHead.getX();
        int oldHeadY = oldHead.getY();

        snake.updatePosForNewFrame();// update snake position
        
        Snake.SnakeNode newHead = snake.getHead();
        int newHeadX = newHead.getX();
        int newHeadY = newHead.getY();
        
        // check if food is found
        FoodStuffs.FoodNode foundFood = foodStuffs.getFoodNode(newHeadX, newHeadY);
        if (foundFood != null) {
            chompSound.play();
            snakeConsume(foundFood);
            if (foodStuffs.gotAllFood())
                initGameWonSequence();
        }

        //check if bad food hit
        if (badFoodStuffs != null) {
            FoodStuffs.FoodNode foundBadFood = badFoodStuffs.getFoodNode(newHeadX, newHeadY);
            if (foundBadFood != null) {
                initGameLostSequence(2);
            }
        }

        // update board visuals
        board.setColor(newHead.getY(), newHead.getX(), SNAKE_HEAD_COLOR);
        board.setColor(oldHeadY, oldHeadX, SNAKE_COLOR); // snake body color
        board.setColor(oldTailY, oldTailX, BOARD_COLOR);
        
        // check if run into self
        if (snake.hasRunIntoSelf())
            initGameLostSequence(1);// end the game
    }

    public void snakeConsume(FoodStuffs.FoodNode f) {
        for (int i = 0; i < f.value; i++)
            snake.grow(); // grow() f.value times
        foodStuffs.remove(f);
    }

    public void startAnimation() {
        musicPlayer.play();
        if (!isGameOver && ! isRunning) {
            isRunning = true;
            //gameStatus = GameStatus.RUNNING;
            board.restoreColorData(boardColorData);
            setFoodColors();
            animator.start();
        }
    }

    public void stopAnimation() {
        musicPlayer.pause();
        if (isRunning){
            isRunning = false;
            animator.stop();
            setPausedText();
        }
    }

    public void initGameLostSequence(int deathFlag) {
        animator.stop();
        musicPlayer.stop();
        deathSound.play();
        //musicPlayer.dispose();
        startButton.setDisable(true);
        pauseButton.setDisable(true);
        isRunning = false;
        isGameOver = true;
        gameStatus = GameStatus.LOST;
        drawDeadSnake();
        if (livesRemaining > 0) {
            setDeathMessage(deathFlag);
        }else {
            setGameOverMessage(deathFlag);
            gameOverMusicPlayer = new MediaPlayer(gameOverMusic);
            gameOverMusicPlayer.play();
            retryButton.setText("NEW GAME");
        }
        retryButton.setDisable(false);
    }

    public void initGameWonSequence() {
        animator.stop();
        musicPlayer.stop();
        startButton.setDisable(true);
        pauseButton.setDisable(true);
        isRunning = false;
        isGameOver = false;
        gameStatus = GameStatus.WON;
        if (level < 5) {// still have more levels to go
            winThemeShortPlayer.play();
            setLevelWonMessage();
            nextLevelButton.setDisable(false);
        }else {
            winThemeLongPlayer = new MediaPlayer(winThemeLong);
            winThemeLongPlayer.play();
            setGameWonMessage();
        }
    }

    public void setScreenText(String text) {
        boardColorData = board.copyColorData();

        GraphicsContext g = board.getGraphicsContext2D();
        g.setFill(Color.BLUE);
        g.setFont(new Font(36));
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText(text, 
                   board.getWidth()/2, board.getHeight()/2);
    }


    public void setSplashScreenText() {
        drawCoolSnake();
        splashScreenMessage = "Collect all Yellow food to win";
        if (badFoodStuffs != null)
            splashScreenMessage += "\nAvoid the RED poison!";
        splashScreenMessage += "\nUse arrow keys to turn";
        splashScreenMessage += "\nClick START or Press SPACE to start!";
        splashScreenMessage += "\nLevel " + level;
        splashScreenMessage += "\nBeat all 5 levels to win!";
        setScreenText(splashScreenMessage);
    }

    public void setPausedText() {
        setScreenText("Game Paused.\nClick START or Press SPACE to continue");
         
    }

    public void setDeathMessage(int deathFlag) {
        String text = "";
        if (deathFlag == 1){ // snake died from eating itself
            text += "Snake has died from eating itself";
        } else if (deathFlag == 2) { // snake died from poison
            text += "Snake has died from eating poison";
        }
        text += "\nSorry, you lose.";
        text += "\nPress RETRY to try this level again.";
        setScreenText(text);
    }

    public void setLevelWonMessage() {
        String text = "Snake got all food.";
        text += "\nYou win this level!";
        text += "\n Press NEXT LEVEL to advance.";
        setScreenText(text);
    }

    public void setGameWonMessage() {
        String text = "Snake got all food.";
        text += "\nCongratulations. You won the game!";
        text += "\nNow go play outside for a bit.";
        setScreenText(text);
    }

    public void setGameOverMessage(int deathFlag) {
        String text = "";
        if (deathFlag == 1){ // snake died from eating itself
            text += "Snake has died from eating itself";
        } else if (deathFlag == 2) { // snake died from poison
            text += "Snake has died from eating poison";
        }
        text += "\nNo lives remaining";
        text += "\nGAME OVER";
        text += "\nPress NEW GAME to start again.";
        setScreenText(text);
    }

    public void setGameStatus(GameStatus status) {
        gameStatus = status;
    }

    public void drawCoolSnake() {
        GraphicsContext g = board.getGraphicsContext2D();
        g.drawImage(coolSnake, 0.35*board.getWidth(), 0, 0.4*411, 0.4*564);
    }

    private void drawDeadSnake() {
        GraphicsContext g = board.getGraphicsContext2D();
        g.drawImage(deadSnake, 0.25*board.getWidth(), -50);
    }

}