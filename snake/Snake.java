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

    // static members
    static final int START_LENGTH = 3;
    static final int MAX_LENGTH = 20;


    // constructor
    public Snake(int startX, int startY, Direction startDir) {
        nodes = new ArrayList<SnakeNode>(MAX_LENGTH);

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

    // utility methods
    public int getSize() {
        return nodes.size();
    }

    public Direction getDirection() {
        return nodes.get(0).direction;
    }

    // update for new frame
    /**
     * Update position for new frame. This means moving
     * each node "forward" by one space. Accomplished by checking
     * the direction and changing x or y accordingly. 
     * Should also call with GameBoard and make sure that 
     * the snake is not going past the boundary. */
    public void updatePosForNewFrame() {
        System.out.println("Updating position of snake...");

        // need direction of current node, and direction of 
        // one node prior, in order to update appropriately
        Direction leadingDirection = getDirection();// for first node

        for (int i = 0; i < nodes.size(); i++) {
            SnakeNode node = nodes.get(i);

            Direction currentDirection = node.direction;
            switch (currentDirection) {
                case UP -> {
                    node.yPos++;
                }
                case DOWN -> {
                    node.yPos--;
                }
                case LEFT -> {
                    node.xPos--;
                }
                case RIGHT -> {
                    node.xPos++;
                }
            }
            // update direction to follow previous node
            node.direction = leadingDirection;
            // update for next node
            leadingDirection = currentDirection;
        }
    } 

    /**
     * Change the snake's direction of motion, relative to game board.
     * Restrict to allow only 90-degree turns (so snake cannot move
     * back on itself)
     */
    public void changeDirection(Direction newDirection) {
        SnakeNode headNode = nodes.get(0);
        Direction currentDirection = headNode.direction;

        switch (currentDirection) {
            case LEFT, RIGHT -> {
                if (newDirection == Direction.UP || newDirection == Direction.DOWN)
                    headNode.direction = newDirection;
            }
            case UP, DOWN -> {
                if (newDirection == Direction.LEFT || newDirection == Direction.RIGHT)
                    headNode.direction = newDirection;
            }
            default -> headNode.direction = newDirection;
        }
    }

    /**
     * Add an additional node to snake, as long as length will be less
     * than maximum. Return boolean value indicating whether snake 
     * was able to add a node (if false, snake may be full).
     * @return boolean indictating whether it was possible to add a node
     */
    public boolean grow() {
        if (getSize() == MAX_LENGTH)
            return false;
        SnakeNode lastNode = nodes.get(getSize()-1);
         
        // fields for new node
        int x, y;
        Direction direction = lastNode.direction; // follow old last node

        switch (direction) {
            case UP -> {
                x = lastNode.xPos;
                y = lastNode.yPos - 1;
            }
            case DOWN -> {
                x = lastNode.xPos;
                y = lastNode.yPos + 1;
            }
            case LEFT -> {
                x = lastNode.xPos + 1;
                y = lastNode.yPos;
            }
            case RIGHT -> {
                x = lastNode.xPos - 1;
                y = lastNode.yPos;
            }
            default -> {
                x = lastNode.xPos;
                y = lastNode.yPos;
            }
        }

        nodes.add(new SnakeNode(x, y, direction));
        System.out.println("Snake grew.");
        return true;
    }
}