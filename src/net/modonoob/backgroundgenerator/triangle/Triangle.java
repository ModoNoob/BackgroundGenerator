package net.modonoob.backgroundgenerator.triangle;

import java.awt.Color;
import java.awt.Polygon;

public class Triangle {
    public Polygon triangle;
    public Color color;
    
    public Triangle(Polygon t, Color c, int a) {
        triangle = t;
        float scale = 1 - (float)a / 255.0f;
        
        float r = (float)c.getRed() / 255.0f;
        float g = (float)c.getGreen() / 255.0f;
        float b = (float)c.getBlue() / 255.0f;
        
        color = new Color(r * scale, g * scale, b * scale);
    }
}
