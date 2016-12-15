package gui;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class Rectangle
        extends java.awt.Rectangle
{
    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    boolean isActive;

    public Rectangle(int x, int y)
    {
        super(x, y);
    }
}
