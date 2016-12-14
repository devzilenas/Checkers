package game;

import java.util.Arrays;

public class Board
{
    public enum Tile
    {
        WHITE, BLACK, EMPTY
    }

    Tile[] board;

    public static Tile[] playableTiles()
    {
        return new Tile[]{Tile.WHITE, Tile.BLACK};
    }

    void initBoard()
    {
        board = new Board.Tile[getWidth()*getHeight()];
        Arrays.fill(board, Tile.EMPTY);
    }

    public Tile[] getBoard()
    {
        return board;
    }

    public void setBoard(Tile[] board)
    {
        this.board = board;
    }

    public int getWidth()
    {
        return width;
    }

    void setWidth(int width)
    {
        this.width = width;
    }

    int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    int width, height;

    Board(int sizex, int sizey)
    {
        this.width = sizex;
        this.height = sizey;
        initBoard();
    }

    public Board()
    {
        this(8,8);
    }

    Tile getTile(int x,int y)
    {
        return getBoard()[getWidth()*x+y];
    }

    void setTile(int x, int y, Tile tile)
    {
        board[getWidth()*x+y] = tile;
    }

    boolean isOccupied(int x, int y)
    {
        return !isEmpty(x,y);
    }

    boolean isEmpty(int x, int y)
    {
        return getTile(x,y).equals(Tile.EMPTY);
    }

}
