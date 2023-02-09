package snake;

import mosaic.MosaicCanvas;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class SnakeGame {
    // fields 
    private MosaicCanvas board;
    private Snake snake;
    private AnimationTimer animator;


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

        snake = new Snake(COLUMNS/2, ROWS/2, Snake.Direction.UP, (int) board.getWidth(), (int) board.getHeight());
        setSnakeColors();
        snake.testSnake();

        animator = new AnimationTimer(){
            long prevFrameTime;
            int frameNumber;
        }

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
            System.out.printf("Set y-coord at %d to Green\n", newHead.getY());
            board.setColor(oldTailY, oldTailX, BOARD_COLOR);
            System.out.printf("Set y-coord at %d to Board color\n", oldTail.getY());
        }
        //setSnakeColors();
    }

    public void updateForNewFrame() {
        // save tail position of snake
        Snake.SnakeNode oldTail = snake.getTail();

        snake.updatePosForNewFrame();// update snake position
        Snake.SnakeNode newHead = snake.getHead();

        board.setColor(newHead.getY(), newHead.getX(), SNAKE_COLOR);
        board.setColor(oldTail.getY(), oldTail.getX(), BOARD_COLOR);


    }

}