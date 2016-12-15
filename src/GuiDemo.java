import gui.Grid;
import gui.GridMouseListener;

import javax.swing.*;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class GuiDemo
{
    static Grid grid;
    public static void showGrid()
    {
        JFrame frame = new JFrame("Checkers client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);

        panel.add(new JLabel("Board"));
        Grid grid = new Grid(400, 400, 8,8);
        grid.playFor("YOUR ARE PLAYNG FOR WHITE");
        grid.addMouseListener(new GridMouseListener());
        panel.add(grid);

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showGrid();
                    }
                }
        );
    }
}
