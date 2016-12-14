package net;

/**
 * Created by m.zilenas on 2016-12-14.
 */
public class ServerCommunicationModule
{
    public ServerConnection[] getConnections()
    {
        return connections;
    }

    ServerConnection[] connections;

    public ServerCommunicationModule(ServerConnection[] connections)
    {
        this.connections = connections;
    }

    /**
     * Say to everyone.
     * @param message
     */
    public void say(String message)
    {
        for(ServerConnection connection : getConnections())
        {
            connection.say(message);
        }
    }

    /**
     * Say to specific communication module.
     * @param connection
     * @param message
     */
    public void say(ServerConnection connection, String message)
    {
        connection.say(message);
    }

    public String readLine(ServerConnection connection)
    {
        return connection.readLine();
    }
}
