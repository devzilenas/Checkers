package gui;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class Checker
        extends Circle
{
    int row;
    int col;

    public CheckerColor getCheckerColor()
    {
        return checkerColor;
    }

    CheckerColor checkerColor;

    public Checker(int row, int col, Rectangle rectangle, CheckerColor color)
    {
        super(rectangle);
        this.row = row;
        this.col = col;
        this.checkerColor = color;
    }

    public static Checker Darker(int row, int col, Rectangle rectangle)
    {
        return new Checker(row, col, rectangle, CheckerColor.BLACK);
    }

    public static Checker Lighter(int row, int col, Rectangle rectangle)
    {
        return new Checker(row, col, rectangle, CheckerColor.WHITE);
    }
}
