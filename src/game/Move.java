package game;

/**
 * Created by m.zilenas on 2016-12-16.
 */
public class Move
{

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

    int xFrom;
    int yFrom;
    int xTo;
    int yTo;

    public Move(int fromRow, int fromCol, int toRow, int toCol)
    {
        this(String.format("%d,%d", fromRow, fromCol), String.format("%d,%d", toRow, toCol));
    }

    /**
     * Expecting strings in format "x,y" (no quotes).
     *
     * @param from
     * @param to
     */
    public Move(String from, String to)
    {
        xFrom = Integer.valueOf(from.split(",")[0]);
        yFrom = Integer.valueOf(from.split(",")[1]);
        xTo = Integer.valueOf(to.split(",")[0]);
        yTo = Integer.valueOf(to.split(",")[1]);
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
