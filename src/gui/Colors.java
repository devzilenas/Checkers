package gui;

import java.awt.*;

/**
 * Created by m.zilenas on 2016-12-15.
 */
public class Colors
{
    //https://en.wikipedia.org/wiki/List_of_colors:_A%E2%80%93F
    public static final Color RECTANGLE_COLOR = new Color(94, 86, 51);//Buff
    public static final Color RECTANGLE_COLOR_ALTERNATE = new Color(48, 71, 38);//Bud Green
    public static final Color RECTANGLE_ACTIVE = new Color(255, 250, 205); //Lemon chiffon

    public static final Color CHECKER_LIGHTER = Color.GRAY;
    public static final Color CHECKER_DARKER = Color.BLACK;
    public static final CheckerColor CHECKER_COLOR_LIGHTER = CheckerColor.WHITE;
    public static final CheckerColor CHECKER_COLOR_DARKER = CheckerColor.BLACK;

    public static Color checkerColor(CheckerColor cc)
    {
        if (cc == CheckerColor.WHITE)
        {
            return CHECKER_LIGHTER;
        } else
        {
            return CHECKER_DARKER;
        }
    }

    boolean a = false;

    public Color alternateColor(Color color2, Color color1)
    {
        Color color = null;
        if (a)
        {
            color = color1;
        } else
        {
            color = color2;
        }
        a = !a;
        return color;
    }
}
