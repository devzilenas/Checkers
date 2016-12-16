package gui;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class Checker
{
    public CheckerColor getCheckerColor()
    {
        return checkerColor;
    }

    public void setCheckerColor(CheckerColor checkerColor)
    {
        this.checkerColor = checkerColor;
    }

    CheckerColor checkerColor;

    public Checker(CheckerColor color)
    {
        this.checkerColor = color;
    }

    public static Checker Darker()
    {
        return new Checker(CheckerColor.BLACK);
    }

    public static Checker Lighter()
    {
        return new Checker(CheckerColor.WHITE);
    }

    public boolean isWhite()
    {
        return getCheckerColor() == CheckerColor.WHITE;
    }

    public boolean isBlack()
    {
        return getCheckerColor() == CheckerColor.BLACK;
    }

    public String toString()
    {
        return getCheckerColor().toString();
    }
}
