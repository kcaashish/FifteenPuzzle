/**
  FifteenPuzzleViewer displays a 15 puzzle and interacts with the user.

  @author Aashish K.C.
 * @version May 2020
 */

import java.awt.event.*;
import java.awt.*;

public class FifteenPuzzleViewer implements MouseListener
{
    private FifteenPuzzle puzzle; // the internal puzzle 
    private int           size;   // the size of the puzzle
    private SimpleCanvas  sc;     // the canvas for display

    private final static Color BACK_COLOUR = Color.decode("#D5E7FF");
    private final static Color TILE_COLOUR = Color.decode("#185566");
    private final static Color GRID_COLOUR = Color.black;

    private int mouseClickX;
    private int mouseClickY;
    private final int tileSize = 100;
    private final int border = 50;
    private final int padding = 30;
    private int gridSize = 0;
    private int squareSize = 0;

    public static void main (String[] args){
        new FifteenPuzzleViewer(new FifteenPuzzle(FifteenPuzzle.close4));
    }
    
    /**
     * Sets up the three fields and draws the initial puzzle.
     */
    public FifteenPuzzleViewer(FifteenPuzzle puzzle) 
    {
        this.puzzle = puzzle;
        size = puzzle.getSize();

        gridSize = size * tileSize;
        squareSize = gridSize + border * 2 + padding * (size - 1);
        sc = new SimpleCanvas("FifteenPuzzle", squareSize,  squareSize + 30, BACK_COLOUR);
        sc.addMouseListener(this);
        fontSize(3);
        drawGrid();
    }

    private void fontSize(double x){
        sc.setFont(sc.getFont().deriveFont((float) (sc.getFont().getSize() * x)));
    }
    
    /**
     * Draws all of the tiles on sc.
     */
    private void drawGrid()
    {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                drawTile(i, j, TILE_COLOUR);
                if ((i == puzzle.getSpaceCoordinate()[0]) && (j == puzzle.getSpaceCoordinate()[1])){
                    drawTile(i, j, BACK_COLOUR);
                }
            }
        }

        for (int i = 0; i <= size; i++){
            int changingLinePos = border / 2 + i * (tileSize + padding) + 10;
            int gridLineEndPos = border / 2 + size * (tileSize + padding) + 10;
            sc.drawLine(changingLinePos, border / 2 + 10, changingLinePos, gridLineEndPos, GRID_COLOUR);
            sc.drawLine(border / 2 + 10, changingLinePos, gridLineEndPos, changingLinePos, GRID_COLOUR);
        }
    }
    
    /**
     * Draws the tile at x,y in colour c at the appropriate spot on sc.
     */
    private void drawTile(int x, int y, Color c)
    {
        Color colour = BACK_COLOUR;
        int top = border + y * (tileSize + padding);
        int bot = border + (y+1) * (tileSize + padding) - padding;
        int left = border + x * (tileSize + padding);
        int right = border + (x+1) * (tileSize + padding) - padding;
        int number = puzzle.getGrid()[x][y];

        for (int i = 0; i < 10; i++){
            if (puzzle.isBlank(x, y)) {
                colour = BACK_COLOUR;
            }
            else {
                colour = c;
            }
            sc.drawRectangle(left - (10-i), top - (10-i), right  + (10-i), bot + (10-i), colour);
        }
        sc.drawRectangle(left, top, right, bot, colour);

        for (int i = 0; i < 10; i++) {
            colour = BACK_COLOUR;
            if (number < 10){
                sc.drawString(number, left + 42, top + 63, colour);
            }
            else {
                sc.drawString(number, left + 30, top + 63, colour);
            }
        }
    }

    private void changeTileColour (int x, int y)
    {
        Color colour;
        int top = border + y * (tileSize + padding);
        int bot = border + (y+1) * (tileSize + padding) - padding;
        int left = border + x * (tileSize + padding);
        int right = border + (x+1) * (tileSize + padding) - padding;
        int number = puzzle.getGrid()[x][y];

        for (int i = 0; i < 30; i++){
            if (puzzle.isBlank(x, y)) {
                colour = BACK_COLOUR;
            }
            else {
                if (puzzle.legalClick(x, y)){
                    colour = Color.green.darker();
                }
                else {
                    colour = Color.decode("#9F0000");
                }
            }
            sc.drawRectangle(left - (10-i), top - (10-i), right  + (10-i), bot + (10-i), colour);
        }

        colour = BACK_COLOUR;
        if (number < 10){
            sc.drawString("" + number, left + 42, top + 63, colour);
        }
        else {
            sc.drawString("" + number, left + 30, top + 63, colour);
        }

    }

    private boolean tileClicked (int x, int y)
    {
        if ((x > border) && x < squareSize - border &&
                !((x < (border + tileSize + padding)) && (x > (border + tileSize))) &&
                !((x < border + 2 * tileSize + 2 * padding) && (x > border + 2 * tileSize + padding)) &&
                !((x < border + 3 * tileSize + 3 * padding) && (x > border + 3 * tileSize + 2 * padding)) &&
                (y > border) && (y < (squareSize - border)) &&
                !((y < border + tileSize + padding) && (y > border + tileSize)) &&
                !((y < border + 2 * tileSize + 2 * padding) && (y > border + 2 * tileSize + padding)) &&
                !((y < border + 3 * tileSize + 3 * padding) && (y > border + 3 * tileSize + 2 * padding)))
        {
            return true;
        }
        return false;
    }

    private void win()
    {
        sc.wait(200);
        sc.drawRectangle(0, 0, squareSize, squareSize+35, Color.decode("#313030"));

        fontSize(1);
        sc.drawString("Congratulations!", (squareSize / 2) - (int)(gridSize / 1.5) + 115, (squareSize / 2) - 100, Color.decode("#FF950E"));
        fontSize(2.5);
        sc.drawString("You win!", (squareSize / 2) - (int)(gridSize / 1.5) + 75, (squareSize / 2) + 10, Color.decode("#FF950E"));

        int resetX1 = (squareSize/2 - 70) - 30;
        int resetX2 = (squareSize / 2 - 70) + 160;
        int resetY1 = squareSize - tileSize + padding;
        int resetY2 = squareSize - padding + 10;
        sc.drawRectangle(resetX1, resetY1, resetX2, resetY2, Color.decode("#03001B"));
        fontSize(0.36);
        sc.drawString("Play Again", squareSize / 2 - 90, squareSize-padding-3, Color.decode("#D5E7FF"));
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!puzzle.finished() && !puzzle.initialising){
            if (tileClicked(e.getX(), e.getY())){
                changeTileColour((e.getX() - border) / (tileSize + padding), (e.getY() - border) / (tileSize + padding));
            }
        }
        mouseClickX = (e.getX() - border) / (tileSize + padding);
        mouseClickY = (e.getY() - border) / (tileSize + padding);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!puzzle.finished() && !puzzle.initialising) {
            if (puzzle.legalClick(mouseClickX, mouseClickY)){
                if (tileClicked(e.getX(), e.getY())){
                    puzzle.moveTile((e.getX() - border) / (tileSize + padding), (e.getY() - border) / (tileSize + padding));
                }
            }
            drawGrid();
            if (puzzle.finished()){
                System.out.println("You Win!");
                win();
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
