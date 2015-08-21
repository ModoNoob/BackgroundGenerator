package net.modonoob.backgroundgenerator.triangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

public class Triangle {
    public Shape triangle;
    public Color color;
    
    public Triangle(Shape t, Color c, int a) {
        triangle = t;
        color = c;
        color = color.darker((float)a / 255f);
    }
}
