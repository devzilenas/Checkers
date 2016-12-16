import net.*;

/**
 * Created by m.zilenas on 2016-12-16.
 */
public class ServerDemo
{
    public static void main(String[] args)
    {
        Server server = new Server(Integer.valueOf(args[0]));
        server.start();
    }
}
