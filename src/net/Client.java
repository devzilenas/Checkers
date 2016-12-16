package net;

import game.Board;
import game.Game;
import game.Move;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client
    extends Thread
{
    ActionListener actionListener;
    public ActionListener getActionListener()
    {
        return actionListener;
    }
    public void setActionListener(ActionListener actionListener)
    {
        this.actionListener = actionListener;
    }

    Game game;
    public void setGame(Game game)
    {
        this.game = game;
    }
    public Game getGame()
    {
        return game;
    }
    public Board getBoard ()
    {
        return getGame().getBoard();
    }

    /**
     * With communication module the client speaks to the socket.
     */
    CommunicationModule communicationModule;
    public void setCommunicationModule(CommunicationModule communicationModule)
    {
        this.communicationModule = communicationModule;
    }

    public CommunicationModule getCommunicationModule()
    {
        return communicationModule;
    }

    public Client(int port)
    {
        try
        {
            createCommunicationModuleOn(new Socket("localhost", port));
            communicationModule.init();

            setGame(new Game());
        }
        catch (IOException e)
        {
            System.err.println("Can not create communication module on port " + port);
            e.printStackTrace();
        }
    }

    public void createCommunicationModuleOn(Socket socket)
    {
        setCommunicationModule(new CommunicationModule(socket));
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol)
    {
        getCommunicationModule().say("MOVE " + new Move(fromRow, fromCol, toRow, toCol).toString2());
    }

    public void informActionListener(String message)
    {
        getActionListener().actionPerformed(new ActionEvent(this, 1, message));
    }

    @Override
    public void run()
    {
        String message;
        do
        {
            message = getCommunicationModule().getLineBlocking();
            IMessageProcessor messageProcessor = new IMessageProcessor()
            {
                public void process(String message)
                {
                    if (null == message)
                        throw new IllegalArgumentException("Message cannot be null");

                    if (message.equals("YOUR TURN"))
                    {
                        informActionListener("YOUR TURN");
                        //getCommunicationModule().say("MOVE 1,1 2,1");
                    } else if (message.startsWith("OPPONENT MOVE "))
                    {
                        informActionListener(message);

                        //Received opponent move
                        // Move received.
                        Scanner scanner = new Scanner(message);
                        scanner.next();
                        scanner.next();//skip words OPPONENT MOVE
                        String from = scanner.next();
                        String to = scanner.next();
                        Move move = new Move(from, to);
                        echo("Client received opponent move: " + move);

                        //write move
                        getBoard().go(move.getxFrom(), move.getyFrom(), move.getxTo(), move.getyTo());
                    }
                    else if (message.equals("YOU ARE PLAYING FOR BLACK"))
                    {
                        informActionListener("YOU ARE PLAYING FOR BLACK");
                    }
                    else if (message.equals("YOU ARE PLAYING FOR WHITE"))
                    {
                        informActionListener("YOU ARE PLAYING FOR WHITE");
                    }
                    else if (message.startsWith("PLEASE OCCUPY SEATS"))
                    {
                        //do whatever
                    }
                    else if (message.equals("WELCOME"))
                    {
                        //do whatever
                    }
                    else
                    {
                        echo(String.format("Don't know how to process message \"%s\"", message));
                    }
                }
                public void echo(String message)
                {
                    System.out.println(message);
                }
            };
            messageProcessor.process(message);
        }
        while (!"END".equals(message));
    }
}