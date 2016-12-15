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

    public Tile winningTile()
    {
        return Tile.NIL;
    }

    public Object winnerIs()
    {
        return winningTile() != Tile.NIL ? occuppantOf(winningTile()) : null;
    }

    public Map<Object, Tile> getSeats()
    {
        return seats;
    }

    Map<Object, Tile> seats = new HashMap<>();

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
    public Tile seatName(Object object)
    {
        return getSeats().get(object);
    }

    public Object occuppantOf(Tile tile)
    {
        Iterator<Map.Entry<Object, Tile>> iterator = getSeats().entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Object, Tile> entry = iterator.next();
            if (entry.getValue() == tile)
            {
                return entry.getKey();
            }
        }
        return null;
    }

    List<Tile> occupiedSeats()
    {
        return new ArrayList<Tile>(getSeats().values());
    }

    public Tile[] getFreeSeats()
    {
        List<Tile> tiles = new ArrayList<Tile>(Arrays.asList(Board.playableTiles()));
        tiles.removeAll(occupiedSeats());
        return tiles.toArray(new Tile[0]);
    }
}
