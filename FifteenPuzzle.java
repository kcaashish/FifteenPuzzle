/*
 * FifteenPuzzle maintains the internal representation of a 15 puzzle. 
 * This is a 4x4 2D array of integers, numbered 0..15. 0 denotes the space. 
 * A legal move is to shift the space to an adjacent square on the grid. 
 * The puzzle is finished when all tiles are in the correct position as defined by goal. 
 * 
 * @author  Aashish K.C.
 * @version May 2020
 */

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class FifteenPuzzle
{ 
    private final int[][] grid; // the current positions of the tiles and the space, denoted by 0..15
    private       int xSpace;   // xSpace,ySpace are the current coordinates of the space
    private       int ySpace;
    private final int size = 4; // the number of tiles across and down 
    private final int[][] goal = {{1,5,9,13}, {2,6,10,14}, {3,7,11,15}, {4,8,12,0}}; // the tile positions in the goal state
    
    // these two are public and static so that they can be used in BlueJ 
    // when creating an object, enter e.g. FifteenPuzzle.close4 in the argument box
    public static final int[][] example4 = {{ 5,11,14, 0}, { 9, 3,13, 7}, { 2, 8,10,12}, { 4, 1,15, 6}};
    public static final int[][] close4   = {{ 1, 5, 9,13}, { 2, 6,10,14}, { 3, 7,11, 0}, { 4, 8,12,15}};

    public boolean initialising = false;

    /** This constructor sets up the grid using initialGrid, and it initialises xSpace and ySpace.
     *  initialGrid is assumed to be a legal position.
     */
    public FifteenPuzzle (int[][] initialGrid)
    {
        grid = initialGrid;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assert grid != null;
                if (grid[i][j] == 0) {
                    xSpace = i;
                    ySpace = j;
                }
            }
        }
    }
    
    /** This constructor sets up the grid by copying goal and then making random moves. 
     *  It also initialises xSpace and ySpace.
     */
    public FifteenPuzzle ()
    {
        initialising = true;
        grid = new int[size][size];

        for (int i = 0; i < size; i++) {
            System.arraycopy(goal[i], 0, grid[i], 0, size);
        }

        xSpace = size - 1;
        ySpace = size - 1;

        for (double i = 0; i < 1600; i++){
            int a = (int) (Math.random() * (double) size);
            int b = (int) (Math.random() * (double) size);

            if (legalClick(a, b)){
                moveTile(a, b);
                try{
                    TimeUnit.MICROSECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        initialising = false;
    }
    
    /**
     * Returns the grid.
     */
    public int[][] getGrid()
    {
        return grid;
    }
       
    /**
     * Returns the size.
     */ 
    public int getSize()
    {
        return size;
    }

    /**
     * Returns true if the current location is blank.
     */
    public boolean isBlank (int x, int y)
    {
        return (grid[x][y] == 0);
    }

    /**
     * Returns the coordinates of the space.
     */
    public int[] getSpaceCoordinate ()
    {
        int[] spaceCoordinate = new int[2];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] == 0) {
                    spaceCoordinate[0] = i;
                    spaceCoordinate[1] = j;
                }
            }
        }
        return spaceCoordinate;
    }
       
    /**
     * Returns true iff x,y is on the board and adjacent to the space. 
     * Either x must be xSpace and y must be ySpace plus or minus 1,
     * or     y must be ySpace and x must be xSpace plus or minus 1.
     */
    public boolean legalClick(int x, int y)
    {
        return Math.abs((x - xSpace)) + Math.abs(y - ySpace) == 1;
    }
    
    /**
     * Returns true iff the puzzle is in the goal state.
     */
    public boolean finished()
    {
        return Arrays.deepEquals(goal, grid);
    }
    
    /**
     * If x,y is a legal click on the board, swaps the tile at x,y with the space;
     * otherwise does nothing.
     */ 
    public void moveTile (int x, int y) 
    {
        if (!(finished())|| initialising) {
            if (legalClick(x, y)) {
                grid[xSpace][ySpace] = grid[x][y];
                grid[x][y] = 0;

                xSpace = x;
                ySpace = y;
            }
        }
    }
}
