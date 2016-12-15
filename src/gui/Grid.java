package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import game.*;
import net.Client;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class Grid
    extends Canvas
    implements ActionListener
{
    Client client;

    int rows;
    int cols;
    Dimension dimension;
    List<Rectangle> rectangles;
    List<Checker>   checkers  ;
    Board board;

    public Tile getMyTile()
    {
        return myTile;
    }

    Tile myTile; //what tile client is playing for

    Checker activeChecker;

    public List<Checker> getCheckers()
    {
        return checkers;
    }

    public void playFor(String msg)
    {
        if (msg.endsWith("BLACK"))
        {
        }
        else if (msg.endsWith("WHITE"))
        {
            setMyTile(Tile.WHITE);
        }
    }

    public int getRectangleRow(Rectangle rectangle)
    {
        return (getRectangles().indexOf(rectangle) - getRectangleCol(rectangle)) / getRows();
    }

    public int getRectangleCol(Rectangle rectangle)
    {
        return getRectangles().indexOf(rectangle) % getCols();
    }

    public Rectangle getRectangleXY(int x, int y)
    {
        Rectangle rectangle = null;

        for (Rectangle r : getRectangles())
        {
            if (r.contains(x,y))
            {
                rectangle = r;
                break;
            }
        }
        return rectangle;
    }

    Rectangle getActiveRectangle()
    {
        Rectangle active = null;
        for (Rectangle r : getRectangles())
        {
            if (r.isActive())
            {
                active = r;
                break;
            }
        }
        return active;
    }

    List<Rectangle> getActiveRectangles()
    {
        List<Rectangle> temporary = new LinkedList<Rectangle>(getRectangles());
        ListIterator<Rectangle> iterator = temporary.listIterator();
        while (iterator.hasNext())
        {
            if (!iterator.next().isActive())
                iterator.remove();
        }
        return temporary;
    }

    public void rectangleClicked(Rectangle r)
    {
        //allow clicking next rectangle
        //this case is when the active already was set. Let's check if it's the next rectangle or the same.
        if (getActiveRectangle() != null)
        {
            //unset previously active rectangle if clicked again on the same
            //this way we can "undo"
            if (getActiveRectangle().equals(r))
            {
                r.setActive(false);
            }
            else //some other rectangle was clicked
            {
                //If client tries to set more than two active rectangles then let client start over fresh
                if (getActiveRectangles().size() == 2)
                    unsetActiveRectangles();
                setActiveRectangle(r);
            }
        }
        else //if there where no active rectangles, then try to set
        {
            setActiveRectangle(r);
        }
    }

    //unsets all active rectangles
    void unsetActiveRectangles()
    {
        for (Rectangle rectangle : getRectangles())
        {
            if (rectangle.isActive())
            {
                rectangle.setActive(false);
            }
        }
    }

    public void setActiveRectangle(Rectangle r)
    {
        int row = getRectangleRow(r);
        int col = getRectangleCol(r);
        Tile tile = getBoard().tile(row,col);
        if(getActiveRectangle() == null) //let it set, because no active previously
        {
            //first check if allowed to set active
            if (tile != getMyTile())
            {
                return;
            }
            else
            {
                r.setActive(true);
            }
        }
        else //there was one rectangle active. suppose that client wants to make a move
        {
            //allow to setactive(true) only that rectangle that checked would conform with checkers rules:
            //empty rectangle, that can be reached by moving in checkers move or jumping over checkers.
            //TODO add checkers rule checking here.

            //first let's check if target is empty rectangle
            if (tile == Tile.NIL)
            {
                //let's remember what was the first selected rectangle
                Rectangle r1 = getActiveRectangle();
                r.setActive(true);

                //now we can initiate a move from one rectangle that has checker to empty rectangle
                int toRow = getRectangleRow(r);
                int toCol = getRectangleCol(r);
                int fromRow = getRectangleRow(r1);
                int fromCol = getRectangleCol(r1);

                //Update board
                getBoard().go(fromRow, fromCol, toRow, toCol);
                //Send message to client
                getClient().makeMove(fromRow, fromCol, toRow, toCol);

                unsetActiveRectangles();
            }
        }
    }

    public void setMyTile(Tile myTile)
    {
        this.myTile = myTile;
    }

    public Grid(int width, int height, int rows, int cols, Client client)
    {
        this.dimension  = new Dimension(width, height);
        this.rows = rows;
        this.cols = cols;
        this.board = new Board(rows, cols);

        this.client = client;
        getClient().start();

        //install itself as action listener for client
        client.setActionListener(this);

        rectangles = new LinkedList<>();
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                Rectangle newRectangle = makeRectangle(row, col);
                getRectangles().add(newRectangle);
            }
        }
    }

    List<Rectangle> getRectangles()
    {
        return rectangles;
    }

    Dimension getDimension()
    {
        return dimension;
    }

    public int getWidth()
    {
        return (int)getDimension().getWidth();
    }

    public int getHeight()
    {
        return (int)getDimension().getHeight();
    }

    int getCols()
    {
        return cols;
    }

    int getRows()
    {
        return rows;
    }

    Checker makeChecker(Tile tile, int row, int col)
    {
        switch (tile)
        {
            case WHITE:
                return Checker.Lighter(row, col, getRectangle(row, col));
            case BLACK:
                return Checker.Darker(row, col, getRectangle(row, col));
        }
        throw new IllegalStateException("Terribly wrong");
    }

    Rectangle makeRectangle(int row, int col)
    {
        Rectangle r = new Rectangle(
                (int) getWidth() / getCols() ,
                (int) getHeight() / getRows());
        r.setLocation((int)(col*r.getWidth()), (int)(row*r.getHeight()));
        return r;
    }

    public Rectangle getRectangle(int row, int col)
    {
        return getRectangles().get(getRows()*row + col);
    }

    public Board getBoard()
    {
        return getClient().getBoard();
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        //Draw rectangles
        Colors colors = new Colors();
        ListIterator iterator = getRectangles().listIterator();
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                Rectangle rectangle = (Rectangle)iterator.next();
                g2.setPaint(colors.alternateColor(Colors.RECTANGLE_COLOR, Colors.RECTANGLE_COLOR_ALTERNATE));
                if (rectangle.isActive())
                {
                    g2.setPaint(Colors.RECTANGLE_ACTIVE);
                }
                g2.fill(rectangle);
            }
            g2.setPaint(colors.alternateColor(Colors.RECTANGLE_COLOR, Colors.RECTANGLE_COLOR_ALTERNATE));
        }

        //Draw checkers
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                Tile tile = getBoard().tile(row, col);
                if (tile != Tile.NIL)
                {
                    Checker checker = makeChecker(tile, row, col);
                    g2.setPaint(Colors.checkerColor(checker.getCheckerColor()));
                    g2.fill(checker);
                }
            }
            g2.setPaint(colors.alternateColor(Colors.RECTANGLE_COLOR, Colors.RECTANGLE_COLOR_ALTERNATE));
        }
    }

    @Override
    public java.awt.Dimension getPreferredSize()
    {
        return new java.awt.Dimension(getWidth(), getHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String message = e.getActionCommand();

        if (message.equals("YOU ARE PLAYING FOR BLACK"))
        {
            setMyTile(Tile.BLACK);
        }
        else if (message.equals("YOU ARE PLAYING FOR WHITE"))
        {
            setMyTile(Tile.WHITE);
        }
        else
        {
        }
        repaint();
    }

    public Client getClient()
    {
        return client;
    }
}
