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

    public boolean isValidMove(Move move)
    {
        return true;
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
