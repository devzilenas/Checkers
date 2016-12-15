package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class GridMouseListener
        implements MouseListener
{
    @Override
    public void mouseClicked(MouseEvent e)
    {
        Grid grid = (Grid) e.getSource();

        Rectangle r = grid.getRectangleXY(e.getX(), e.getY());
        grid.rectangleClicked(r);

        grid.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }
}
