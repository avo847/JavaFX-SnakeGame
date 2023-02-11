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
            value = (int) (3*Math.random())+1;
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

    private ArrayList<FoodNode> nodes;

    public FoodStuffs(int boardWidth, int boardHeight, int startNumber) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.startNumber = startNumber;

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