package game;

import gui.Checker;
import gui.CheckerColor;

import java.util.*;

/**
 * Created by m.zilenas on 2016-12-14.
 */
public class Game
{
    Board board;

    public Board getBoard()
    {
        return board;
    }
    public Game()
    {
        board = new Board();
    }
    public Checker winningChecker()
    {
        return null;
    }

    public Object winnerIs()
    {
        return winningChecker() == null ? null : occuppantOf(winningChecker().getCheckerColor()) ;
    }

    public Map<Object, CheckerColor> getSeats()
    {
        return seats;
    }

    Map<Object, CheckerColor> seats = new HashMap<>();

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

        public Move(int fromRow, int fromCol, int toRow, int toCol)
        {
            this(String.format("%d,%d", fromRow, fromCol), String.format("%d,%d", toRow, toCol));
        }

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
    public CheckerColor seatName(Object object)
    {
        return getSeats().get(object);
    }

    public Object occuppantOf(CheckerColor color)
    {
        Iterator<Map.Entry<Object, CheckerColor>> iterator = getSeats().entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Object, CheckerColor> entry = iterator.next();
            if (entry.getValue() == color)
            {
                return entry.getKey();
            }
        }
        return null;
    }

    List<CheckerColor> occupiedSeats()
    {
        return new ArrayList<CheckerColor>(getSeats().values());
    }

    public CheckerColor[] getFreeSeats()
    {
        List<CheckerColor> colors = new ArrayList<>(Arrays.asList(Board.playableCheckers()));
        colors.removeAll(occupiedSeats());
        return colors.toArray(new CheckerColor[0]);
    }
}
