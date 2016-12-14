package net;

import com.sun.javaws.exceptions.InvalidArgumentException;
import game.Game;
import sun.plugin2.message.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client
    extends Thread
{

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    Game game;

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

    public CommunicationModule getCommunicationModule()
    {
        return communicationModule;
    }

    public void createCommunicationModuleOn(Socket socket)
    {
        setCommunicationModule(new CommunicationModule(socket));
    }

    CommunicationModule communicationModule;

    @Override
    public void run()
    {
        String message;
        do
        {
            message = getCommunicationModule().getLineBlocking();
            MessageProcessor messageProcessor = new MessageProcessor();
            messageProcessor.process(message);
        }
        while (!"END".equals(message));

    }

    public void setCommunicationModule(CommunicationModule communicationModule)
    {
        this.communicationModule = communicationModule;
    }

    public class MessageProcessor
    {
        public MessageProcessor()
        {
        }

        public void process(String message)
        {
            if (null == message)
                throw new IllegalArgumentException("Messsage cannot be null");
            if (message.equals("YOUR TURN"))
            {
                getCommunicationModule().say("MOVE 1,1 2,1");
            }
            else if (message.startsWith("OPPONENT MOVE "))
            {
                //Received opponent move
                // Move received.
                Scanner scanner = new Scanner(message);
                scanner.next(); scanner.next();//skip words OPPONENT MOVE
                String from = scanner.next();
                String to   = scanner.next();
                Game.Move move = getGame(). new Move(from, to);
                echo("Received opponent move: " + move);
            }
            else if (message.startsWith("YOU ARE PLAYING FOR"))
            {
                echo(message);
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
    }
}