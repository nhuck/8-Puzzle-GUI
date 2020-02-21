// Course:          CS4242
// Student name:    Noah Huck
// Assignment #:    #1
// Due Date:        02/12/2020

package hw1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * AStar - Class to create <b>Nodes</b> and do A* algorithm to find solution
 * steps
 */
public class AStar {
    int[][] rootGrid;
    Node root;

    private ArrayList<Node> open = new ArrayList<Node>();
    private ArrayList<Node> close = new ArrayList<Node>();

    public AStar() {
        rootGrid = new int[3][3];
        populateSample();
        root = new Node(rootGrid, 0, null, 0);
        open.add(root);
    }

    public AStar(int[][] grid) {
        rootGrid = grid;
        root = new Node(rootGrid, 0, null, 0);
        open.add(root);
    }

    /**
     * populateSample() - creates sample grid from the assignment .pdf:
     * 2,8,3,1,6,4,7,0,5
     */
    private void populateSample() {
        rootGrid[0][0] = 2;
        rootGrid[0][1] = 8;
        rootGrid[0][2] = 3;
        rootGrid[1][0] = 1;
        rootGrid[1][1] = 6;
        rootGrid[1][2] = 4;
        rootGrid[2][0] = 7;
        rootGrid[2][1] = 0;
        rootGrid[2][2] = 5;
    }

    // Debug Method
    /*
     * private static String nodeData(Node n) { return
     * String.format("G Value: %d, F Value: %d", n.G, n.F); }
     */

    /**
     * doStar() - implementation of the A * algorithm
     * 
     * @return String containing comma separated list of moves in integer form
     */
    public String doStar() {
        System.out.println("Original Grid: ");
        root.printGrid();

        boolean solution = false;
        root.createChildren();
        open.remove(root);
        close.add(root);
        Node current = root;

        // Nodes will continue to be expanded until a solution is found
        while (!solution) {
            for (Node child : current.getChildren()) {
                // Checks each child of the current node to see if solution has been found
                if (child.H == 0) {
                    System.out.println("Completed Grid: ");
                    child.printGrid();
                    System.out.println(new String(new char[30]).replace('\0', '*'));
                    close.add(child);
                    // recursive function to get moves from each parent
                    return (child.exit());
                }
                // Each child node added to the list of open nodes
                open.add(child);
            }

            Node next = open.get(0);

            // Searches through open nodes to find one with best heuristic or lowest number
            // of moves completed
            for (Node n : open) {
                if (n.F < next.F) {
                    next = n;
                } else if (n.F == next.F) {
                    if (n.G < next.G) {
                        next = n;
                    }
                }
            }
            // expanded node added to closed node list, loop continues
            next.createChildren();
            close.add(next);
            open.remove(next);
            current = next;
        }
        return "";
    }

    /**
     * solveable( int[][] ) - used to determine if a given grid can be solved to the
     * desired solution state
     * 
     * @param input - grid to be checked for validity
     * @return true if grid can be solved, false if grid cannot be solved
     */
    public static boolean solveable(int[][] input) {
        int inversions = 0;
        int[] grid1D = new int[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid1D[i + j] = input[i][j];
            }
        }
        for (int i = 0; i < 9; ++i) {
            if (grid1D[i] == 0)
                continue;
            for (int j = i + 1; j < 9; ++j) {
                if (grid1D[j] != 0 && grid1D[i] > grid1D[j])
                    inversions++;
            }
        }
        return (inversions % 2 != 0);
    }

    /**
     * command() - runs a command-line version of the program
     */
    public void command() {
        System.out.println("Input Grid.");
        Scanner in = new Scanner(System.in);
        int[][] input = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                input[i][j] = in.nextInt();
            }
        }
        in.close();
        if (solveable(input)) {
            AStar a = new AStar(input);
            String solution = a.doStar();
            System.out.println(String.format("\n%d closed nodes.", a.close.size()));
            System.out.println(String.format("%d moves required.\n", a.close.get(a.close.size() - 1).G));

            for (String s : moves(solution)) {
                System.out.print(s);
            }
        } else {
            System.out.println("That is an unsolvable grid. Try running again.");
            System.exit(0);
        }
    }

    /**
     * moves(String) - translates a string of moves in integer form to a more
     * readable form
     * 
     * @param solution - output from <code>doStar()</code>
     * @return String[] sequence of ways to move blank to achieve solution
     */
    public String[] moves(String solution) {
        String[] rawMoves = solution.split(",");
        String[] moves = new String[rawMoves.length];
        for (int i = 0; i < rawMoves.length; i++) {
            switch (rawMoves[i]) {
                case "origin": {
                    moves[i] = "Begin. ";
                    break;
                }
                case "-2": {
                    moves[i] = "Move Blank Up.   ";
                    break;
                }
                case "2": {
                    moves[i] = "Move Blank Down.   ";
                    break;
                }
                case "-3": {
                    moves[i] = "Move Blank Left.   ";
                    break;
                }
                case "3": {
                    moves[i] = "Move Blank Right.   ";
                    break;
                }
            }
            if (i % 7 == 0) {
                moves[i] = moves[i].concat("\n");
            }
        }
        return moves;
    }

    /**
     * getClosedNodesSize()
     * 
     * @return value of close.size()
     */
    public int getClosedNodesSize() {
        return close.size();
    }

}