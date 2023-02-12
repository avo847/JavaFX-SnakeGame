package snake;

import java.util.ArrayList;

public class Snake {
    // direction relative to game boards
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE;
        
        public static boolean areOpposites(Direction d1, Direction d2) {
            boolean isOpposite = switch (d1) {
                case UP -> (d2 == Direction.DOWN);
                case DOWN -> (d2 == Direction.UP);
                case LEFT -> (d2 == Direction.RIGHT);
                case RIGHT -> (d2 == Direction.LEFT);
                default -> false;
            };
            return isOpposite;
        }
    };

    // nested class for snake node
    public static class SnakeNode {
        int xPos, yPos; // position on grid
        Direction direction;

        // constructor
        public SnakeNode(int x, int y, Direction direction) {
            this.xPos = x;
            this.yPos = y;
            this.direction = direction;
        }

        int getX() {
            return xPos;
        }

        int getY() {
            return yPos;
        }

        /**
          * For checking if snake has come into contact
          * with itself.
         */
        public boolean hasSameLocation(SnakeNode s) {
            return this.xPos == s.getX() && this.yPos == s.getY();
        }

    }

    // Fields for snake
    private ArrayList<SnakeNode> nodes;
    private int boardWidth, boardHeight;

    // static members
    static final int START_LENGTH = 5;
    static final int MAX_LENGTH = 100;


    // constructor
    public Snake(int startX, int startY, Direction startDir, int boardWidth, int boardHeight) {

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        nodes = new ArrayList<SnakeNode>(MAX_LENGTH);

        // initialize nodes
        int x = startX; 
        int y = startY;
        for (int i = 0; i < START_LENGTH; i++) {
            nodes.add( new SnakeNode(x, y, startDir) );
            
            // update position for next node
            if (startDir == Direction.UP)
                y++; // decrement y by 1
            else if (startDir == Direction.DOWN)
                y--;
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

    /**
     * Set the snake's direction manually. Only for internal
     * use, specifically for correcting course when running 
     * into boundary.
     */
    public void setSnakeDirection(Direction newDirection) {
        nodes.get(0).direction = newDirection;
    }

    public SnakeNode getHead() {
        return nodes.get(0);
    }

    public SnakeNode getTail() {
        return nodes.get(nodes.size() - 1);
    }

    public int[] getXdirections() {
        int[] xs = new int[nodes.size()];

        for (int i = 0; i < nodes.size(); i++)
            xs[i] = nodes.get(i).getX();

        return xs;
    }


    public int[] getYdirections() {
        int[] ys = new int[nodes.size()];

        for (int i = 0; i < nodes.size(); i++)
            ys[i] = nodes.get(i).getY();

        return ys;
    }

    public boolean hasNodeAtLocation(int x, int y) {
        for (SnakeNode node : nodes) {
            if (x == node.getX() && y == node.getY()) {
                return true;
            }
        }
        return false;
    }

    // update for new frame
    /**
     * Update position for new frame. This means moving
     * each node "forward" by one space. Accomplished by checking
     * the direction and changing x or y accordingly. 
     * Should also call with GameBoard and make sure that 
     * the snake is not going past the boundary. */
    public void updatePosForNewFrame() {

        // need direction of current node, and direction of 
        // one node prior, in order to update appropriately
        Direction leadingDirection = getDirection();// for first node
        int headX = nodes.get(0).getX();
        int headY = nodes.get(0).getY();

        switch (leadingDirection) {
            case UP -> {
                if (headY == 0) {
                    if (headX == 0)
                        setSnakeDirection(Direction.RIGHT);
                    else if (headX == boardWidth-1)
                        setSnakeDirection(Direction.LEFT);
                    else
                        setSnakeDirection(turnNinetyDegreesAtRandom(leadingDirection));
                }
            }
            case DOWN -> {
                if (headY == boardHeight-1) {
                    if (headX == 0)
                        setSnakeDirection(Direction.RIGHT);
                    else if (headX == boardWidth-1)
                        setSnakeDirection(Direction.LEFT);
                    else
                        setSnakeDirection(turnNinetyDegreesAtRandom(leadingDirection));
                }
            }
            case LEFT -> {
                if (headX == 0) {
                    if (headY == 0)
                        setSnakeDirection(Direction.DOWN);
                    else if (headY == boardHeight-1)
                        setSnakeDirection(Direction.UP);
                    else
                        setSnakeDirection(turnNinetyDegreesAtRandom(leadingDirection));
                }
            }
            case RIGHT -> {
                if (headX == boardWidth-1) {
                    if (headY == 0)
                        setSnakeDirection(Direction.DOWN);
                    else if (headY == boardHeight-1)
                        setSnakeDirection(Direction.UP);
                    else
                        setSnakeDirection(turnNinetyDegreesAtRandom(leadingDirection));//turn up or down
                }
            }
        }
        leadingDirection = getDirection();

        for (int i = 0; i < nodes.size(); i++) {
            SnakeNode node = nodes.get(i);

            Direction currentDirection = node.direction;
            switch (currentDirection) {
                case UP -> {
                    node.yPos--;
                }
                case DOWN -> {
                    node.yPos++;
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
                y = lastNode.yPos + 1;
            }
            case DOWN -> {
                x = lastNode.xPos;
                y = lastNode.yPos - 1;
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
        return true;
    }

    /**
     * Move manually. Intended to be linked to key events.
     * Returns true if move was successful, and false otherwise.
     */
    public boolean keyMove(Direction keyDirection) {
        Direction currentDirection = nodes.get(0).direction;

        if (Direction.areOpposites(currentDirection, keyDirection)) {
            return false; // do nothing
        } else if (currentDirection == keyDirection) {
            updatePosForNewFrame();
            return true;
        } else {// need to turn
            changeDirection(keyDirection);
            updatePosForNewFrame();
            return true;
        }
    }

    /**
     * Turn 90 degrees from current direction at random (roughly
     * equal odds each way). For use when snake runs into a wall
     * head-on, but not a corner.
     */
    public Direction turnNinetyDegreesAtRandom(Direction initDir) {
        if (initDir == Direction.LEFT || initDir == Direction.RIGHT) //  move up or down
            return (Math.random() > 0.5) ? Direction.UP : Direction.DOWN;
        else if (initDir == Direction.UP || initDir == Direction.DOWN)
            return (Math.random() > 0.5) ? Direction.LEFT : Direction.RIGHT;
        else
            return initDir;
    }

    /**
     * For checking whether snake has run into itself.
     */
    public boolean hasRunIntoSelf() {
        SnakeNode headNode = getHead();

        for (int i = 1; i < nodes.size(); i++) {
            if (headNode.hasSameLocation(nodes.get(i)))
                return true;
        }
        return false;
    }

}