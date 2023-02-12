package snake;

import java.util.ArrayList;

/**
 * This class represents the food on the board, as 
 * an arraylist of FoodNodes.
 */
public class FoodStuffs {

    // nested class
    public static class FoodNode {
        public int x, y;
        public int value; // how much food this counts for

        public FoodNode(int x, int y) {
            this.x = x;
            this.y = y;
            value = (int) (5*Math.random())+1;
        }

        boolean equals(FoodNode f) {
            return f.x == this.x && f.y == this.y;
        }

        boolean matchCoords(int x, int y) {
            return this.x == x && this.y == y;
        }
    }

    // static members
    public static int INIT_NUMBER = 20;// initial numbers

    private int boardWidth;// should probably change to cols and rows
    private int boardHeight;
    private int startNumber;
    private Snake snake; // refence to snake for checking coordinates
    private FoodStuffs otherFoodStuffs; // reference to existing foodstuffs
                                        // as this class will represent both
                                        // good and bad food, there might be
                                        // up to two instances present at once.

    private ArrayList<FoodNode> nodes;

    /**
     * Create startNumber FoodNodes that randomly populate the grid. Should really say
     * columns and rows. Reference to snake is needed to make sure there are no 
     * overlapping coordinates (otherwise the node would disappear from the grid, 
     * and player would have to go over it again without seeing it)
     */
    public FoodStuffs(int boardWidth, int boardHeight, int startNumber, Snake snake, 
                        FoodStuffs otherFoodStuffs) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.startNumber = startNumber;
        this.snake = snake;
        this.otherFoodStuffs = otherFoodStuffs;

        if (startNumber < 1)
            startNumber = INIT_NUMBER;
        nodes = new ArrayList<FoodNode>(startNumber);

        for (int i = 0; i < startNumber; i++) {
            nodes.add(newRandomNode());
        }
    }

    public void printNodes() {  // for testing
        for (FoodNode node : nodes)
            System.out.printf("FoodNode at (%d,%d)\n", node.x, node.y);
    }

    /**
     * Generate a random node
     */
    private FoodNode newRandomNode() {
        int x, y; 
        boolean nodeExists;
        do {
            x = (int) (boardWidth * Math.random());
            y = (int) (boardHeight * Math.random());
            nodeExists = nodePresent(x,y) || nodeOverlapsSnake(x,y);
            if(otherFoodStuffs != null) //also check if it exists there
                nodeExists = nodeExists || otherFoodStuffs.nodePresent(x,y);
        } while (nodeExists);
        return new FoodNode(x,y);
    }

    public boolean nodePresent(int x, int y) {
        if (nodes == null) // no nodes yet
            return false;

        for (FoodNode node : nodes) {
            if (node.matchCoords(x,y))
                return true;
        }
        return false;
    }

    private boolean nodeOverlapsSnake(int x, int y) {
        if (snake == null) {
            System.out.println("No snake!");
            return false;
        }

        return snake.hasNodeAtLocation(x,y);
    }

    public FoodNode getFoodNode(int x, int y) {
        if (nodes == null) // no nodes yet
            return null;

        for (FoodNode node : nodes) {
            if (node.matchCoords(x,y))
                return node;
        }
        return null;
    }

    public boolean remove(FoodNode f) {
        return nodes.remove(f);
    }

    public ArrayList<FoodNode> getFoodList() {
        return nodes;
    }

    public boolean gotAllFood() {
        return nodes.size() == 0;
    }

}