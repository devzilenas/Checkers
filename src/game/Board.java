package game;

import gui.Checker;
import gui.CheckerColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class Board
{
    Tile[] tiles = null;
    int rows;
    int cols;

    public int getRows()
    {
        return rows;
    }
    public int getCols()
    {
        return cols;
    }

    public static CheckerColor[] playableCheckers()
    {
        return new CheckerColor[]{CheckerColor.WHITE, CheckerColor.BLACK};
    }

    public void go(Move move)
    {
        go(move.getxFrom(), move.getyFrom(), move.getxTo(), move.getyTo());

        //remove captured
        if (move.isCapture())
        {
            Tile capture = move.getCapture();
            capture.setChecker(null);
        }
    }

    public void go(int rowFrom, int colFrom, int rowTo, int colTo)
    {
        Tile movingTile = getTile(rowFrom, colFrom);
        Checker checker = movingTile.getChecker();

        Tile movingToTile = getTile(rowTo, colTo);

        //let's move a checker form one tile to another
        movingTile.setChecker(null);
        movingToTile.setChecker(checker);
    }

    void initBoard()
    {
        //Make empty tiles
        for (int i = 0; i < getTiles().length; i++)
        {
            getTiles()[i] = Tile.EMPTY();
        }

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                if ( (col + (row  % 2)) % 2 != 0)
                {
                    put(Tile.BLACK(), row, col);
                }
            }
        }

        for (int row = getRows() - 3; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                if ((col  + (row % 2)) % 2 != 0)
                {
                    put(Tile.WHITE(), row, col);
                }
            }
        }
    }

    public Tile[] getTiles()
    {
        return tiles;
    }

    public void setTiles(Tile[] tiles)
    {
        this.tiles = tiles;
    }

    public int getWidth()
    {
        return getCols();
    }

    int getHeight()
    {
        return getRows();
    }

    public Board(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Tile[getRows()*getCols()];
        initBoard();
    }
    public Board()
    {
        this(8,8);
    }
    void put(Tile tile, int atRow, int atCol)
    {
        tiles[getRows()*atRow+atCol] = tile;
    }
    Tile get(int row, int col)
    {
        return getTiles()[getRows()*row + col];
    }
    public Tile tile(int row, int col)
    {
        return get(row, col);
    }
    Tile getTile(int x,int y)
    {
        return tile(x,y);
    }
    void setTile(int x, int y, Tile tile)
    {
       put(tile,x,y);
    }
    boolean isOccupied(int x, int y)
    {
        return !isEmpty(x,y);
    }
    boolean isEmpty(int x, int y)
    {
        return getTile(x,y).isEmpty();
    }
    public boolean validTile(int row, int col)
    {
        return row >= 0 && row < getWidth() && col >= 0 && col < getHeight();
    }

    /**
     * Returns all left checkers of given color.
     * @param color
     * @return
     */
    Collection<Tile> getTilesWithCheckersOf(CheckerColor color)
    {
        Collection<Tile> tiles = new LinkedList<>();
        for (Tile tile : getTiles())
        {
            if (tile.isOccupied() && tile.getCheckerColor() == color)
            {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    /**
     * Gets row of tile.
     * @param tile
     * @return
     */
    public int tileRow(Tile tile)
    {
        return (Arrays.asList(getTiles()).indexOf(tile) - tileCol(tile)) / getCols();
    }

    public int tileCol(Tile tile)
    {
        return Arrays.asList(getTiles()).indexOf(tile) % getCols();
    }
}
