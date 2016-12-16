package gui;

/**
 * Created by m.zilenas on 2016-12-16.
 */
public class CheckerCircle
        extends Circle
{
    int row;
    int col;

    public Checker getChecker()
    {
        return checker;
    }
    Checker checker;

    public CheckerCircle(int row, int col, Rectangle rectangle, Checker checker)
    {
        super(rectangle);
        this.row = row;
        this.col = col;
        this.checker = checker;
    }
}
