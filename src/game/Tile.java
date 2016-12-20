package game;

import gui.Checker;
import gui.CheckerColor;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class Tile
{
    public static Tile EMPTY()
    {
        return new Tile();
    }

    public static Tile WHITE()
    {
        return new Tile(Checker.Lighter());
    }

    public static Tile BLACK()
    {
        return new Tile(Checker.Darker());
    }

    public Checker getChecker()
    {
        return checker;
    }

    public void setChecker(Checker checker)
    {
        this.checker = checker;
    }

    Checker checker;

    public Tile()
    {

    }

    public Tile(Checker checker)
    {
        this.checker = checker;
    }

    public boolean isOccupied()
    {
        return !isEmpty();
    }

    public boolean isEmpty()
    {
        return checker==null;
    }

    public CheckerColor getCheckerColor()
    {
        if (isEmpty())
        {
            throw new IllegalStateException("Checker not found!");
        }
        else
        {
            return getChecker().getCheckerColor();
        }
    }

    public boolean colorEquals(CheckerColor checkerColor)
    {
        return isEmpty() ? false : getChecker().getCheckerColor() == checkerColor;
    }

    public boolean hasColor(CheckerColor color)
    {
        return isEmpty() ? false : getChecker().getCheckerColor() == color;
    }
}

