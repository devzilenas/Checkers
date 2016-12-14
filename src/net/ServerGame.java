package net;

import com.sun.javaws.exceptions.InvalidArgumentException;
import game.*;

import java.io.IOException;
import java.net.ServerSocket;
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

        MessageProcessor processor = new MessageProcessor();

        ServerConnection winner = null;
        while (!winningcondition())
        {
            say(player1(), "YOUR TURN");
            processor.process(readLine(player1()), player1());

            if (winningCondition()) //Check for winning condition. NOTE that there might be no possible move so then wins the one who moved last.
            {
                winner = player1();
                break;
            }

            say(player2(), "YOUR TURN");
            processor.process(readLine(player2()), player2());
            if (winningCondition())
            {
                winner = player2();
                break;
            }
        }
        anounceWinner(winner);
    }

    public boolean winningcondition()
    {
        //Check if no checkers left or no move possible.
        return false;
    }

    public void anounceWinner(ServerConnection player)
    {
        say(opponentOf(player), "YOU LOOSE!");
        say(player, "YOU WIN!");
    }

    public class MessageProcessor
    {
        public MessageProcessor()
        {
        }

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
                Game.Move move = getGame(). new Move(from, to);
                System.out.println("Received client move: " + move);

                //Pass this move to opponent
                say(opponentOf(player),"OPPONENT MOVE " + move.toString2());
            }
            else
            {
                echo(String.format("Don't know how to process message %s", message));
            }
        }

        public void echo(String message)
        {
            System.out.println(String.format("Processing '%s'", message));
        }
    }

    public boolean winningCondition()
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
        return getConnections()[0].equals(sc) ? getConnections()[1] : getConnections()[1];
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
