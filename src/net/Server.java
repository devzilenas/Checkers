package net;

public class Server
    extends Thread
{
    CommunicationModule com;
    ServerGame serverGame;

    public Server(int port)
    {
        serverGame = new ServerGame(port);
    }

    @Override
    public void run()
    {
        serverGame.start();
    }
}