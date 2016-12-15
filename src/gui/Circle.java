package gui;

public class Circle
        extends java.awt.geom.Ellipse2D.Double
{
    Rectangle rectangle;
    //pass a framing rectangle to the circle
    public Circle(Rectangle rectangle)
    {
        super(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        this.rectangle = rectangle;
    }
}

