/**
 * Created by m.zilenas on 2016-12-14.
 */
import net.*;

public class GameDemo
{
    public static final int PORT = 50001;
    public static void main(String[] args)
    {
        Server server = new Server(PORT);

        Client client1 = new Client(PORT);
        Client client2 = new Client(PORT);

        server.start();
        client1.start();
        client2.start();
    }
}
