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

        public FoodNode(int x, int y) {
            this.x = x;
            this.y = y;
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

    private ArrayList<FoodNode> nodes;

    public FoodStuffs(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        nodes = new ArrayList<FoodNode>(INIT_NUMBER);

        for (int i = 0; i < INIT_NUMBER; i++) {
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
        do {
            x = (int) (boardWidth * Math.random());
            y = (int) (boardHeight * Math.random());
        } while (nodePresent(x,y));
        System.out.printf("New random node at (%d,%d)\n", x, y);

        return new FoodNode(x,y);
    }

    private boolean nodePresent(int x, int y) {
        if (nodes == null) // no nodes yet
            return false;

        for (FoodNode node : nodes) {
            if (node.matchCoords(x,y))
                return true;
        }
        return false;
    }

    public boolean isFoodNode(int x, int y) {
        if (nodes == null) // no nodes yet
            return false;

        for (FoodNode node : nodes) {
            if (node.matchCoords(x,y))
                return true;
        }
        return false;
    }



}