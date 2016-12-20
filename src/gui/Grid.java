package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
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
    List<CheckerCircle>   circles;

    boolean myTurn; //this is set to True when "YOUR TURN" message is received.

    CheckerCircle activeCircle;
    CheckerColor myColor; //what color client is playing for

    public void setMyColor(CheckerColor color)
    {
        this.myColor = color;
    }
    public CheckerColor getMyColor()
    {
        return myColor;
    }

    public List<CheckerCircle> getCircles()
    {
        return circles;
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
        List<Rectangle> temporary = new LinkedList<>(getRectangles());
        ListIterator<Rectangle> iterator = temporary.listIterator();
        while (iterator.hasNext())
        {
            if (!iterator.next().isActive())
                iterator.remove();
        }
        return temporary;
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
            //Empty tile do not set active
            if (tile.isEmpty())
            {
                return;
            }

            // if not my color do not allow to activate
            if (!tile.colorEquals(getMyColor()))
            {
                return;
            }

            //first check if allowed to move
            if (!getGame().allMoves(tile).isEmpty())
            {
                //if there are capturing moves let activate only them
                if (getGame().hasCaptures(getMyColor()))
                {
                    //see if from this tile is capturing
                    if (getGame().hasCaptures(tile))
                    {
                        r.setActive(true);
                    }
                }
                else //let activate any
                {
                    r.setActive(true);
                }
            }
            else
            {
                return;
            }
        }
        else //there was one rectangle active. suppose that client wants to make a move
        {
            //allow to setactive(true) only that rectangle that checked would conform with checkers rules:
            //empty rectangle, that can be reached by moving in checkers move or jumping over checkers.

            //first let's check if target is empty rectangle
            if (tile.isEmpty())
            {
                //let's remember what was the first selected rectangle
                Rectangle r1 = getActiveRectangle();

                //now we can initiate a move from one rectangle that has checker to empty rectangle
                int   toRow = getRectangleRow(r);
                int   toCol = getRectangleCol(r);
                int fromRow = getRectangleRow(r1);
                int fromCol = getRectangleCol(r1);

                List<Move> allMoves = new LinkedList<>(getGame().allMoves(fromRow, fromCol));
                Move tempMove = new Move(fromRow, fromCol, toRow, toCol);//it is temporary move because it contains no capture information.

                if (!allMoves.contains(tempMove)) //if allMoves do not contain this move. Then this move is invalid.
                {
                    //simply return doing nothing
                    return;
                }

                Move move = allMoves.get(allMoves.indexOf(tempMove));//get move with capture information

                //set current rectangle as active
                r.setActive(true);

                //if move is valid
                //Update board
                getBoard().go(move);
                //Send message to client
                getClient().makeMove(move);

                setMyTurn(false);

                //in any case (valid or not valid move)
                unsetActiveRectangles();
            }
        }
    }

    public Grid(int width, int height, int rows, int cols, Client client)
    {
        this.dimension  = new Dimension(width, height);
        this.rows = rows;
        this.cols = cols;

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

    CheckerCircle makeCheckerCircle(Tile tile, int row, int col)
    {
        Checker checker = tile.getChecker();
        return new CheckerCircle(row, col, getRectangle(row, col), checker);
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

    public Game getGame()
    {
        return getClient().getGame();
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
                if (tile.isOccupied())
                {
                    CheckerCircle checkerCircle = makeCheckerCircle(tile, row, col);
                    g2.setPaint(Colors.checkerColor(checkerCircle.getChecker().getCheckerColor()));
                    g2.fill(checkerCircle);
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

    /**
     * This is where messages are received. Parse them from here.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String message = e.getActionCommand();

        if (message.equals("YOU ARE PLAYING FOR BLACK"))
        {
            setMyColor(CheckerColor.BLACK);
        }
        else if (message.equals("YOU ARE PLAYING FOR WHITE"))
        {
            setMyColor(CheckerColor.WHITE);
        }
        else if (message.equals("YOUR TURN"))
        {
            setMyTurn(true); //once set to true the grid allows to click a rectangle. See rectangleClicked() method below.
            //make turn
        }
        else if (message.startsWith("OPPONENT MOVE "))
        {
            //does nothing
        }
        repaint();
    }

    public void rectangleClicked(Rectangle r)
    {
        if (!isMyTurn())//do not react to rectangle clicks if not my turn.
        {
            return;
        }

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
                {
                    unsetActiveRectangles();
                }

                //Now set active rectangle
                setActiveRectangle(r);
            }
        } else //if there where no active rectangles, then try to set
        {
            setActiveRectangle(r);
        }
    }

    public Client getClient()
    {
        return client;
    }

    public boolean isMyTurn()
    {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn)
    {
        this.myTurn = myTurn;
    }
}
