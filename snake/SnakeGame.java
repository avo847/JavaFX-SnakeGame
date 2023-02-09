package snake;

import mosaic.MosaicCanvas;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class SnakeGame {
    // fields 
    private MosaicCanvas board;
    private Snake snake;
    private AnimationTimer animator;

    private int gameSpeed;


    private final static int ROWS = 40; // rows in the mosaic
    private final static int COLUMNS = 40; 
    private final static int SQUARE_SIZE = 15;// size of each square
    private final static Color BOARD_COLOR = Color.DARKGRAY;
    private final static Color SNAKE_COLOR = Color.GREEN;
    private final static Color SNAKE_HEAD_COLOR = Color.DARKGREEN;

    public SnakeGame() {
        board = new MosaicCanvas(ROWS, COLUMNS, SQUARE_SIZE, SQUARE_SIZE);
        board.fill(BOARD_COLOR);
        board.setGroutingColor(null);

        snake = new Snake(COLUMNS/2, ROWS/2, Snake.Direction.UP, COLUMNS, ROWS);
        setSnakeColors();
        snake.testSnake();

        gameSpeed = 8;// squares moved per second

        animator = new AnimationTimer(){
            long prevFrameTime;
        

            public void handle(long time) {
                if (time - prevFrameTime > 0.99e9/gameSpeed) {// 1 fps
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
    public void setSnakeColors() {
        board.fill(BOARD_COLOR);
        int[] xs = snake.getXdirections();
        int[] ys = snake.getYdirections();

        board.setColor(ys[0], xs[0], SNAKE_HEAD_COLOR);

        for (int i = 1; i < snake.getSize(); i++) {
            board.setColor(ys[i], xs[i], SNAKE_COLOR);
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
        System.out.printf("OldTail at (%d,%d)\n", oldTail.getX(), oldTail.getY());
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

        board.setColor(newHead.getY(), newHead.getX(), SNAKE_HEAD_COLOR);
        board.setColor(oldHeadY, oldHeadX, SNAKE_COLOR); // snake body color
        board.setColor(oldTailY, oldTailX, BOARD_COLOR);


    }

    public void startAnimation() {
        animator.start();
    }

    public void stopAnimation() {
        animator.stop();
    }

}