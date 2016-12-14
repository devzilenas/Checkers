package game;

import java.util.*;

/**
 * Created by m.zilenas on 2016-12-14.
 */
public class Game
{
    Board board;

    public Game()
    {
        board = new Board();
    }

    public Board.Tile winningTile()
    {
        return Board.Tile.EMPTY;
    }

    public Object winnerIs()
    {
        return winningTile() != Board.Tile.EMPTY ? occuppantOf(winningTile()) : null;
    }

    public Map<Object, Board.Tile> getSeats()
    {
        return seats;
    }

    Map<Object, Board.Tile> seats = new HashMap<>();

    public class Move
    {
        int xFrom;

        public int getxFrom()
        {
            return xFrom;
        }

        public void setxFrom(int xFrom)
        {
            this.xFrom = xFrom;
        }

        public int getyFrom()
        {
            return yFrom;
        }

        public void setyFrom(int yFrom)
        {
            this.yFrom = yFrom;
        }

        public int getxTo()
        {
            return xTo;
        }

        public void setxTo(int xTo)
        {
            this.xTo = xTo;
        }

        public int getyTo()
        {
            return yTo;
        }

        public void setyTo(int yTo)
        {
            this.yTo = yTo;
        }

        int yFrom;
        int xTo  ;
        int yTo  ;

        /**
         * Expecting strings in format "x,y" (no quotes).
         * @param from
         * @param to
         */
        public Move(String from, String to)
        {
            xFrom = Integer.valueOf(from.split(",")[0]);
            yFrom = Integer.valueOf(from.split(",")[1]);
            xTo   = Integer.valueOf(to.split(",")[0]);
            yTo   = Integer.valueOf(to.split(",")[1]);
        }

        public String toString2()
        {
            return String.format("%d,%d %d,%d", xFrom, yFrom, xTo, yTo);
        }

        public String toString()
        {
            return String.format("A move from [%d,%d] to [%d,%d].", xFrom, yFrom, xTo, yTo);
        }
    }

    public void occupySeat(Object object)
    {
        getFreeSeats();
        seats.put(object, getFreeSeats()[0]); //put to first free seat
    }

    /*
     * Tells which seat is occupied by Player
     */
    public Board.Tile seatName(Object object)
    {
        return getSeats().get(object);
    }

    public Object occuppantOf(Board.Tile tile)
    {
        Iterator<Map.Entry<Object, Board.Tile>> iterator = getSeats().entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Object, Board.Tile> entry = iterator.next();
            if (entry.getValue() == tile)
            {
                return entry.getKey();
            }
        }
        return null;
    }

    List<Board.Tile> occupiedSeats()
    {
        return new ArrayList<Board.Tile>(getSeats().values());
    }

    public Board.Tile[] getFreeSeats()
    {
        List<Board.Tile> tiles = new ArrayList<Board.Tile>(Arrays.asList(Board.playableTiles()));
        tiles.removeAll(occupiedSeats());
        return tiles.toArray(new Board.Tile[0]);
    }
}
