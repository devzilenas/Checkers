import gui.Grid;
import gui.GridMouseListener;
import net.Client;
import net.Server;

import javax.swing.*;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class GuiDemo
{
    public static final int PORT = 50001;
    public static void showGrid(String what)
    {
        JFrame frame = new JFrame("Checkers client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);

        panel.add(new JLabel("Board"));
        Client client = new Client(PORT);
        Grid grid = new Grid(400, 400, 8, 8, client);
        grid.addMouseListener(new GridMouseListener());
        panel.add(grid);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        Server server = new Server(PORT);
        server.start();
        javax.swing.SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showGrid("BLACK");
                    }
                }
        );

        javax.swing.SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showGrid("WHITE");
                    }
                }
        );
    }
}
