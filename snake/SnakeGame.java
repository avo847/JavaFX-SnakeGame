package snake;

import mosaic.MosaicCanvas;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SnakeGame {

    public enum GameStatus {RUNNING, PAUSED, WON, LOST};
    // fields 
    private MosaicCanvas board;
    private Snake snake;
    private FoodStuffs foodStuffs;
    private AnimationTimer animator;

    private String splashScreenMessage;
    private int level;
    private int gameSpeed;
    private boolean isRunning;
    private boolean isGameOver;
    public GameStatus gameStatus;
    private int framesPerUpdate;

    private Object boardColorData;
    private Button nextLevelButton;

    private final static int ROWS = 40; // rows in the mosaic
    private final static int COLUMNS = 40; 
    private final static int SQUARE_SIZE = 15;// size of each square
    private final static Color BOARD_COLOR = Color.DARKGRAY;
    private final static Color SNAKE_COLOR = Color.GREEN;
    private final static Color SNAKE_HEAD_COLOR = Color.DARKGREEN;
    private final static Color FOOD_COLOR = Color.YELLOW;
    private final static int MAX_LEVEL = 5;

    public SnakeGame(int level, Button nextLevelButton) {
        board = new MosaicCanvas(ROWS, COLUMNS, SQUARE_SIZE, SQUARE_SIZE);
        this.level = (level > 0) ? level : 1;
        board.fill(BOARD_COLOR);
        board.setGroutingColor(null);
        this.nextLevelButton = nextLevelButton;

        snake = new Snake(COLUMNS/2, ROWS/2, Snake.Direction.UP, COLUMNS, ROWS);

        foodStuffs = new FoodStuffs(COLUMNS, ROWS, 5*level);

        //gameSpeed = 8;// squares moved per second
        //gameSpeed = 60 + 13 * (level - 5);
        gameSpeed = switch (level) {
            case 1 -> 8;
            case 2 -> 10;
            case 3 -> 15;
            case 4 -> 25;
            case 5 -> 45;
            default -> 60;
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
        
        System.out.println("Keypress registered");
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
        
        // check if food is found
        FoodStuffs.FoodNode foundFood = foodStuffs.getFoodNode(newHead.getX(), newHead.getY());
        if (foundFood != null) {
            snakeConsume(foundFood);
            if (foodStuffs.gotAllFood())
                initGameWonSequence();
        }
        // update board visuals
        board.setColor(newHead.getY(), newHead.getX(), SNAKE_HEAD_COLOR);
        board.setColor(oldHeadY, oldHeadX, SNAKE_COLOR); // snake body color
        board.setColor(oldTailY, oldTailX, BOARD_COLOR);
        
        // check if run into self
        if (snake.hasRunIntoSelf())
            initGameLostSequence();// end the game
    }

    public void snakeConsume(FoodStuffs.FoodNode f) {
        for (int i = 0; i < f.value; i++)
            snake.grow(); // grow() f.value times
        foodStuffs.remove(f);
    }

    public void startAnimation() {
        if (!isGameOver && ! isRunning) {
            isRunning = true;
            gameStatus = GameStatus.RUNNING;
            board.restoreColorData(boardColorData);
            setFoodColors();
            animator.start();
        }
    }

    public void stopAnimation() {
        if (isRunning){
            isRunning = false;
            animator.stop();
            setPausedText();
        }
    }

    public void initGameLostSequence() {
        animator.stop();
        isRunning = false;
        isGameOver = true;
        gameStatus = GameStatus.LOST;
        setDeathMessage();
    }

    public void initGameWonSequence() {
        animator.stop();
        isRunning = false;
        isGameOver = false;
        gameStatus = GameStatus.WON;
        if (level < 5) {// still have more levels to go
            setLevelWonMessage();
            nextLevelButton.setDisable(false);
        }else {
            setGameWonMessage();
        }
    }

    public void setScreenText(String text) {
        boardColorData = board.copyColorData();

        GraphicsContext g = board.getGraphicsContext2D();
        g.setFill(Color.BLUE);
        g.setFont(new Font(40));
        g.setTextAlign(TextAlignment.CENTER);
        g.fillText(text, 
                   board.getWidth()/2, board.getHeight()/2);

    }
    public void setSplashScreenText() {
        splashScreenMessage = "Press START to start!";
        splashScreenMessage += "\nLevel " + level;
        splashScreenMessage += "\nBeat all 5 levels to win!";
        setScreenText(splashScreenMessage);
    }

    public void setPausedText() {
        setScreenText("Game Paused.\nPress START to continue");
    }

    public void setDeathMessage() {
        String text = "Snake has died from eating itself";
        text += "\nSorry, you lose.";
        text += "\nPress Reset to start again.";
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
    }

}