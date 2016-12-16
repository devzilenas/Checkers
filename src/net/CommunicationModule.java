package net;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

public class CommunicationModule
{
    private Socket         socket;
    private PrintWriter    out   ;
    private BufferedReader in    ;

    public CommunicationModule(Socket socket)
    {
        this.socket = socket;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public void setOut(PrintWriter out)
    {
        this.out = out;
    }

    public PrintWriter getOut()
    {
        return out;
    }

    public void setIn(BufferedReader in)
    {
        this.in = in;
    }

    public BufferedReader getIn()
    {
        return in;
    }

    public void init()
    {
        try
        {
            setOut(new PrintWriter(
                    getSocket().getOutputStream(), true));
            setIn(new BufferedReader(
                    new InputStreamReader(
                            getSocket().getInputStream())));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String readLine()
    {
        String ret = null;
        try
        {
            ret = in.readLine();
        }
        catch (IOException e)
        {
            System.err.println("Could not read from socket."+e);
        }
        return ret;
    }

    public String readLineBlocking()
    {
        return getLineBlocking();
    }

    public void say(String msg)
    {
        while (null == getOut())
        {
            System.err.println("Out not initialized yet!");
        }
        getOut().println(msg);
    }

    public boolean isConnected()
    {
        return getSocket().isConnected();
    }

    String getLineBlocking()
    {
        String str = null;
        while (null == getIn())
        {
            System.out.println("In not initialized yet!");
        }
        while (null == (str = readLine()))
        {
        }
        return str;
    }
}
