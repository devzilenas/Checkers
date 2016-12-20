package net;

import game.*;
import gui.CheckerColor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.Scanner;

/**
 * Created by m.zilenas on 2016-12-14.
 */
public class ServerGame
    extends Thread
{
    public Game getGame()
    {
        return game;
    }

    Game game;

    public ServerCommunicationModule getCommunicationModule()
    {
        return communicationModule;
    }

    ServerCommunicationModule communicationModule;

    public ServerConnection[] getConnections()
    {
        return connections;
    }

    ServerConnection[] connections;

    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    ServerSocket serverSocket;

    public ServerGame(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Could not open server socket.");
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void run()
    {
        while (!isConnected())
            waitForConnections();

        //Past this point connections are available. Start game.
        setGame(new Game());

        say("WELCOME");
        say("PLEASE OCCUPY SEATS");

        getGame().occupySeat(player1());
        getGame().occupySeat(player2());

        say(player1(), "YOU ARE PLAYING FOR " + getGame().seatName(player1()));
        say(player2(), "YOU ARE PLAYING FOR " + getGame().seatName(player2()));

        IMessageProcessor processor = new IMessageProcessor()
        {
            public Move getLastMove()
            {
                return lastMove;
            }

            public void setLastMove(Move lastMove)
            {
                this.lastMove = lastMove;
            }

            public Move lastMove = null;

            public boolean isCaptureOnLastMove()
            {
                return getLastMove().isCapture();
            }

            @Override
            public void process(String message)
            {
                //does nothing
            }

            @Override
            public void process(String message, ServerConnection player)
            {
                if (message == null)
                    throw new IllegalArgumentException("Message cannot be null.");

                if (message.startsWith("MOVE "))
                {
                    // Move received.
                    Scanner scanner = new Scanner(message);
                    scanner.next();//skip word MOVE
                    String from = scanner.next();
                    String to   = scanner.next();
                    Move tempMove = new Move(from, to);
                    System.out.println("Server received client move: " + tempMove);
                    Move move = getGame().capture(tempMove); //get move with capture information if move has capture.
                    setLastMove(move);

                    //Pass this move to opponent
                    say(opponentOf(player),"OPPONENT MOVE " + move.toString2());

                    //update board

                    getGame().getBoard().go(move);
                }
                else
                {
                    throw new IllegalArgumentException(
                        String.format("Don't know how to process message %s", message));
                }
            }
        };

        ServerConnection winner = null;
        ServerConnection currentPlayer = player1(); //current moving player
        boolean hasMoved = false;
        while (winner == null)
        {
            while (!hasMoved || (processor.isCaptureOnLastMove() && hasCaptures(currentPlayer)))
            {
                say(currentPlayer, "YOUR TURN");

                //get move from current player
                processor.process(
                        readLine(currentPlayer), currentPlayer);

                winner = getWinner(currentPlayer);
                if (winner != null)
                {
                    break;
                }
                hasMoved = true;
            }
            currentPlayer = opponentOf(currentPlayer);
            hasMoved = false;

            // check draw and break if draw
        }

        if (winner != null)
        {
            anounceWinner(winner);
        }
        else
        {
            //anounce draw
        }
    }

    private boolean hasCaptures(ServerConnection currentPlayer)
    {
        return getGame().hasCaptures
               (
                   getGame().seatName(currentPlayer)
               );
    }

    //Moving allowed if has moves
    private boolean movingAllowed(ServerConnection currentPlayer)
    {
        CheckerColor color = getGame().seatName(currentPlayer);
        return getGame().getAnyAllowedMove(color) != null;
    }

    public void anounceWinner(ServerConnection player)
    {
        say(opponentOf(player), "YOU LOOSE!");
        say(player, "YOU WIN!");
    }

    /**
     * Returns a winner or null if no winner yet.
     * @return
     */
    ServerConnection getWinner(ServerConnection lastMovedPlayer)
    {
        //See if opponent of lastMovedPlayer has any checkers left.

        //Check if no checkers left or no move possible.
        CheckerColor color = getGame().seatName(opponentOf(lastMovedPlayer));
        Collection<Tile> withCheckers = getGame().getTilesWithCheckersOf(color);

        //Opponent of last moved player has no checkers left then lastMovedPlayer has won.
        if (withCheckers.isEmpty())
        {
            return lastMovedPlayer;
        }

        return null;
    }

    /**
     * Check if player has won or lost.
     * @param player
     * @return
     */
    public boolean winningCondition(ServerConnection player)
    {

        return false;
    }

    ServerConnection player(int i)
    {
        return getConnections()[i];
    }

    public ServerConnection player1()
    {
        return player(0);
    }

    public ServerConnection player2()
    {
        return opponentOf(player1());
    }

    public ServerConnection opponentOf(ServerConnection sc)
    {
        return getConnections()[0].equals(sc) ? getConnections()[1] : getConnections()[0];
    }

    private boolean isConnected()
    {
        return getConnections() != null
                && getConnections().length == 2
                && player1().isConnected()
                && player2().isConnected();
    }

    public void waitForConnections()
    {
        try
        {
            connections = new ServerConnection[]
                    {
                            new ServerConnection(getServerSocket().accept()) //wait player1
                            , new ServerConnection(getServerSocket().accept()) //wait player2

                    };
            setCommunicationModule(new ServerCommunicationModule(connections));
        }
        catch (IOException e)
        {
            System.err.println("Could not accept connections.");
            e.printStackTrace(System.err);
        }
    }
    public void say(ServerConnection toPlayer, String aMessage)
    {
        getCommunicationModule().say(toPlayer, aMessage);
    }

    public void say(String message)
    {
        getCommunicationModule().say(message);
    }

    public String readLine(ServerConnection fromPlayer)
    {
        return getCommunicationModule().readLine(fromPlayer);
    }

    public void setCommunicationModule(ServerCommunicationModule communicationModule)
    {
        this.communicationModule = communicationModule;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }
}
