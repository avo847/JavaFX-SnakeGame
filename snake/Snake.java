package snake;

import java.util.ArrayList;

public class Snake {
    // direction relative to game boards
    public enum Direction {UP, DOWN, LEFT, RIGHT, NONE};

    // nested class for snake node
    class SnakeNode {
        int xPos, yPos; // position on grid
        Direction direction;

        // constructor
        SnakeNode(int x, int y, Direction direction) {
            this.xPos = x;
            this.yPos = y;
            this.direction = direction;
        }

    }

    // Fields for snake
    private ArrayList<SnakeNode> nodes;
    private Direction direction;
    

    // static members
    static final int START_LENGTH = 3;
    static final int MAX_LENGTH = 20;


    // constructor
    public Snake(int startX, int startY, Direction startDir) {
        nodes = new ArrayList<SnakeNode>(MAX_LENGTH);
        this.direction = startDir;

        // initialize nodes
        int x = startX; 
        int y = startY;
        for (int i = 0; i < START_LENGTH; i++) {
            nodes.add( new SnakeNode(x, y, startDir) );
            
            // update position for next node
            if (startDir == Direction.UP)
                y--; // decrement y by 1
            else if (startDir == Direction.DOWN)
                y++;
            else if (startDir == Direction.LEFT)
                x++;
            else if (startDir == Direction.RIGHT)
                x--;
            else // Direction.NONE
                throw new IllegalArgumentException("No direction given for snake");
        }
    }// end constructor

    // test the snake setup
    public void testSnake() {
        System.out.println("Snake with " + nodes.size() + " nodes.");
        for (int i = 0; i < nodes.size(); i++) {
            System.out.printf("Node %d at (x, y) = (%d,%d)\n", i, 
                                nodes.get(i).xPos, nodes.get(i).yPos);
        }
    }
}