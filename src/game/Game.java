package game;

import gui.Checker;
import gui.CheckerColor;
import net.ServerConnection;

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

    public Collection<Move> getCapturingMovesFor(CheckerColor color)
    {
        return capturingMoves
               (
                   movesFromTiles
                   (
                       getTilesWithCheckersOf(color)
                   )
               );
    }

    public Collection<Move> capturingMoves(Collection<Move> moves)
    {
        Collection<Move> capturingMoves = new LinkedList<>(moves);
        Iterator<Move> iterator = capturingMoves.iterator();

        while (iterator.hasNext())
        {
            if (!iterator.next().isCapture())
            {
                iterator.remove();
            }
        }
        return capturingMoves;
    }

    public Collection<Move> allMoves(Tile tile)
    {
        return allMoves(getBoard().tileRow(tile), getBoard().tileCol(tile));
    }

    public boolean hasCaptures(CheckerColor color)
    {
        return !getCapturingMovesFor(color).isEmpty();
    }

    /**
     * Returns all moves for the position.
     * @param fromRow
     * @param fromCol
     * @return
     */
    public Collection<Move> allMoves(int fromRow, int fromCol)
    {
        Collection<Move> moves = new LinkedList<>();

        Tile       tile = getBoard().get(fromRow, fromCol);
        Checker checker = tile.getChecker();
        int direction = checker.isBlack() ? +1 : -1; //direction is actual only for men

        int x = fromRow;
        int y = fromCol;

        if (checker.isMan())
        {
            int coll = fromCol;
            int colr = fromCol;

            //Get positions
            //Check two rows
            x = x + direction * 1; //get next row
            int xNext = x + direction * 1;

            //get two cols
            coll = coll - 1;
            colr = colr + 1;

            int collnext = coll - 1;
            int colrnext = colr + 1;

            if (getBoard().validTile(x, coll))
            {
                Tile tilel = getBoard().get(x, coll);
                if (tilel.isEmpty())
                {
                    moves.add(
                            new Move(fromRow, fromCol, x, coll));
                }
                else if (getBoard().validTile(xNext, collnext) && getBoard().get(xNext, collnext).isEmpty())
                {
                    Checker checkerl = tilel.getChecker();
                    //if opposite then can jump
                    if (checkerl.getCheckerColor() != checker.getCheckerColor()) //capture
                    {
                        moves.add(
                                new Move(fromRow, fromCol, xNext, collnext, tilel));
                    }
                }
            }
            if (getBoard().validTile(x, colr))
            {
                Tile tiler = getBoard().get(x, colr);
                if (tiler.isEmpty())
                {
                    moves.add(new Move(fromRow, fromCol, x, colr));
                }
                else if (getBoard().validTile(xNext, colrnext) && getBoard().get(xNext,colrnext).isEmpty())
                {
                    Checker checkerr = tiler.getChecker();
                    //if opposite then can jump
                    if (checkerr.getCheckerColor() != checker.getCheckerColor()) //capture
                    {
                        moves.add(
                                new Move(fromRow, fromCol, xNext, colrnext, tiler));
                    }
                }
            }
        }
        else if (checker.isKing()) //check all four directions
        {
            //top left direction
            x = fromRow - 1;
            y = fromCol - 1;
            moves.addAll(
                    collectKingMoves(fromRow, fromCol, -1, -1));
            moves.addAll(
                    collectKingMoves(fromRow, fromCol, -1, +1));
            moves.addAll(
                    collectKingMoves(fromRow, fromCol, +1, +1));
            moves.addAll(
                    collectKingMoves(fromRow, fromCol, +1, -1));
        }

        return moves;
    }

    Collection<Move> collectKingMoves(int fromRow, int fromCol, int directionRow, int directionCol)
    {
        Checker checker = getBoard().get(fromRow, fromCol).getChecker();

        Collection<Move> moves = new LinkedList<>();

        int x = fromRow + directionRow*1;
        int y = fromCol + directionCol*1;
        int xNext = x + directionRow*1;
        int yNext = y + directionCol*1;
        Tile foundOccupied = null;

        while (getBoard().validTile(x,y))
        {
            Tile    tileC = getBoard().get(x,y); //checked tile
            Tile tileNext = getBoard().validTile(xNext, yNext) ? getBoard().get(xNext, yNext) : null; //next tile to the checked tile

            if (tileC.isEmpty())
            {
                moves.add(new Move(fromRow, fromCol, x, y, foundOccupied));
                x = x + directionRow*1;
                y = y + directionCol*1;
            }
            else if (tileC.isOccupied())
            {
                if (foundOccupied != null) //there was occupied found before. so abort processing.
                {
                    break;
                }
                else
                {
                    foundOccupied = tileC;
                }

                if (tileNext != null && tileNext.isEmpty() //next is empty then this can be jumped over
                        && tileC.getCheckerColor() != checker.getCheckerColor()) //if different color
                {
                    x = xNext;
                    y = yNext;
                    // do not break as the move will be collected in next iteration
                }
                else //there is block abort walking in this direction
                {
                    break;
                }

            }
            xNext = x + directionRow*1;
            yNext = y + directionCol*1;
        }

        return moves;
    }

    public Collection<Tile> getTilesWithCheckersOf(CheckerColor color)
    {
        return getBoard().getTilesWithCheckersOf(color);
    }

    /**
     * All moves from given tiles.
     * @return
     */
    public Collection<Move> movesFromTiles(Collection<Tile> tiles)
    {
        Collection<Move> moves = new LinkedList<>();
        for(Tile tile : tiles)
        {
            moves.addAll(allMoves(tile));
        }

        return moves;
    }

    /**
     * Find first allowed move
     * @return
     */
    public Move getAnyAllowedMove(CheckerColor color)
    {
        Collection<Tile> tiles = getBoard().getTilesWithCheckersOf(color);
        Iterator iterator = tiles.iterator();
        while (iterator.hasNext())
        {
            Tile tile = (Tile) iterator.next();
            Collection<Move> allMoves = allMoves(tile);
            if (!allMoves.isEmpty())
            {
                return allMoves.iterator().next();
            }
        }
        return null;
    }

    public void occupySeat(Object object)
    {
        getFreeSeats();
        seats.put(object, getFreeSeats()[0]); //put to first free seat
    }

    /**
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

    /**
     * For the given move returns the move with capture tile if there is capture. If there is no capture original move is returned.
     * @param move
     * @return
     */
    public Move capture(Move move)
    {
        Move withCapture = move;
        Collection<Move> captures = new LinkedList<>
        (
            capturesFrom
            (
                getBoard().tile
                (
                    move.getxFrom(), move.getyFrom()
                )
            )
        );

        Iterator<Move> iterator = captures.iterator();
        while(iterator.hasNext())
        {
            Move capture = iterator.next();
            //if move is not equal capture move then take capture.
            if (capture.equals(move))
            {
                withCapture = capture;
                break;
            }
        }

        return withCapture;
    }

    public boolean isMoveCapturing(Move move)
    {
        Collection<Move> moves = new LinkedList<>();
        moves.add(move);
        return !capturingMoves(moves).isEmpty();
    }

    /**
     * True if captures from the given tile.
     * @param tile
     * @return
     */
    public Collection<Move> capturesFrom(Tile tile)
    {
        Collection<Move> moves = new LinkedList<>(allMoves(tile));
        Iterator<Move> iterator = moves.iterator();
        while(iterator.hasNext())
        {
            if (!iterator.next().isCapture())
            {
                iterator.remove();
            }
        }
        return moves;
    }

    public boolean hasCaptures(Tile tile)
    {
        return !capturesFrom(tile).isEmpty();
    }
}
