// Course:          CS4242
// Student name:    Noah Huck
// Assignment #:    #1
// Due Date:        02/12/2020

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Node - Single state of the 8 puzzle problem
 */
public class Node {

    public final int G;
    private final int sourceDirection;
    private int[][] grid;
    public final int F;
    public final int H;
    private ArrayList<Node> children;
    public Node parent;

    public final int UP = -2;
    public final int DOWN = 2;
    public final int LEFT = -3;
    public final int RIGHT = 3;

    public int[] directions = { UP, DOWN, LEFT, RIGHT };

    public Node(int[][] grid, int G, Node parent, int direction) {
        this.grid = grid;
        this.G = G;
        this.parent = parent;
        this.sourceDirection = direction;
        this.F = this.getH() + this.G;
        this.H = this.getH();
        this.children = new ArrayList<Node>();
    }

    /**
     * Called when solution has been found. Appends the direction used to reach this
     * node to the String returned by parent.
     * 
     * @return String - .csv sequence of moves
     */
    public String exit() {
        if (parent == null) {
            return "origin";
        } else {
            return String.format("%s,%d", parent.exit(), sourceDirection);
        }
    }

    /**
     * createChildren() - Creates a new <code>Node</code> instance for every valid
     * direction the blank can be moved excluding the reverse of the direction used
     * to get to this state. New <code>Node</code> instances added to ArrayList
     * <code>children</code>
     */
    public void createChildren() {
        for (int d : directions) {
            if (canMoveBlank(d) && ((sourceDirection != ((-1) * d)) || sourceDirection == 0)) {
                // System.out.println("Can move in direction: " + d);
                int negDirection = (sourceDirection * -1);
                if (d != negDirection || sourceDirection == 0) {
                    children.add(new Node(swapBlank(d), this.G + 1, this, d));
                } else {
                    System.out.println("Will not move in source direction");
                }
            }
        }
    }

    /**
     * getChildren()
     * 
     * @return ArrayList<Node> of all children Nodes of <code>this</code> (max 3)
     */
    public ArrayList<Node> getChildren() {
        return this.children;
    }

    /**
     * findBlankX()
     * 
     * @return The row value for the blank space
     */
    private int findBlankX() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * findBlankY()
     * 
     * @return The column value for the blank space
     */
    private int findBlankY() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == 0) {
                    return j;
                }
            }
        }
        return -1;
    }

    /**
     * canMoveBlank(int) - After finding the blank location, validity of a move to
     * direction <code>dir</code> is assessed.
     * 
     * @param dir - Direction to be assessed
     * @return Can the blank be moved in direction <code>dir</code>
     */
    public boolean canMoveBlank(int dir) {
        switch (dir) {
            case UP: {
                if (findBlankX() == 0) {
                    return false;
                }
                break;
            }
            case DOWN: {
                if (findBlankX() == 2) {
                    return false;
                }
                break;
            }
            case LEFT: {
                if (findBlankY() == 0) {
                    return false;
                }
                break;
            }
            case RIGHT: {
                if (findBlankY() == 2) {
                    return false;
                }
                break;
            }
        }
        return true;
    }

    /**
     * swapBlank(int) - calls <code>swap()</code> for two numbers using a direction
     * as an input
     * 
     * @param direction - for swap to be done
     * @return new int[][] after swap has been completed
     */
    public int[][] swapBlank(int direction) {
        switch (direction) {
            case UP: {
                int bX = findBlankX();
                int bY = findBlankY();
                return swap(bX, bY, bX - 1, bY);
            }
            case DOWN: {
                int bX = findBlankX();
                int bY = findBlankY();
                return swap(bX, bY, bX + 1, bY);
            }
            case LEFT: {
                int bX = findBlankX();
                int bY = findBlankY();
                return swap(bX, bY, bX, bY - 1);
            }
            case RIGHT: {
                int bX = findBlankX();
                int bY = findBlankY();
                return swap(bX, bY, bX, bY + 1);
            }
        }
        return grid;
    }

    /**
     * swap(int, int, int, int) - after copying the local grid to a new int[][],
     * swaps two numbers
     * 
     * @param x1 - row value for first number to be swapped
     * @param y1 - col value for first number to be swapped
     * @param x2 - row value for second number to be swapped
     * @param y2 - col value for second number to be swapped
     * @return
     */
    private int[][] swap(int x1, int y1, int x2, int y2) {
        int[][] out = new int[3][];
        for (int i = 0; i < 3; i++) {
            out[i] = Arrays.copyOf(grid[i], grid[i].length);
        }
        int temp = out[x1][y1];
        out[x1][y1] = out[x2][y2];
        out[x2][y2] = temp;
        return out;
    }

    /**
     * Uses the <code>scoreTile()</code> method to find the total Manhattan-score
     * value of all combined tiles.
     * 
     * @return int - Total Manhattan-score of grid
     */
    private int getH() {
        int totalScore = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                totalScore += scoreTile(i, j, grid[i][j]);
            }
        }
        return totalScore;
    }

    /**
     * Called by <code>getH()</code> to obtain Manhattan-score by comparing each
     * value in grid with the correct position of that value
     * 
     * @param row - row value of <b>val</b>
     * @param col - column value of <b>val</b>
     * @param val - value at <code>grid[row][col]</code>
     * @return int - Manhattan-score for the input value
     */
    private int scoreTile(int row, int col, int val) {
        switch (val) {
            case 1: {
                int rRow = 0;
                int rCol = 0;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            case 2: {
                int rRow = 0;
                int rCol = 1;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            case 3: {
                int rRow = 0;
                int rCol = 2;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            case 4: {
                int rRow = 1;
                int rCol = 2;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            case 5: {
                int rRow = 2;
                int rCol = 2;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            case 6: {
                int rRow = 2;
                int rCol = 1;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            case 7: {
                int rRow = 2;
                int rCol = 0;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            case 8: {
                int rRow = 1;
                int rCol = 0;
                int rowScore = Math.abs(rRow - row);
                int colScore = Math.abs(rCol - col);
                return rowScore + colScore;
            }
            default: {
                return 0;
            }
        }
    }

    /**
     * Prints the input grid to <b>System.out</b>
     * 
     */
    public void printGrid() {
        for (int i = 0; i < 3; i++) {
            System.out.print("[ ");
            for (int j = 0; j < 3; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.print("]\n");
        }
    }
}
