package net.modonoob.backgroundgenerator.generator;

import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;
import delaunay_triangulation.Triangle_dt;
import java.awt.Color;
import java.awt.Polygon;
import java.util.Iterator;
import java.util.Random;
import net.modonoob.backgroundgenerator.triangle.Triangle;

public class BackgroundGenerator {
    private final int nbOfPoints = 100;
    private final int proximityRange = 50;
    private final Color[] globalColors;
    private final Random R;
    
    private static int width;
    private static int height;
    
    private Point_dt[] points;
    
    private Triangle_dt[] triangles;
    private Triangle[] renderedTriangles;
    
    private Delaunay_Triangulation triangulation;
    
    public BackgroundGenerator(int w, int h) {
        width = w;
        height = h;
        
        points = new Point_dt[nbOfPoints];
        globalColors = new Color[2];
        R = new Random();
    }
    
    public Triangle[] generate() {
        generatePoints();
        generateColors();
        triangulate();
        
        return renderedTriangles;
    }
    
    private void generatePoints() {
        points = new Point_dt[nbOfPoints];
        points[0] = new Point_dt(0, 0);
        points[1] = new Point_dt(0, height);
        points[2] = new Point_dt(width, 0);
        points[3] = new Point_dt(width, height);
        for(int i = 4; i < nbOfPoints; i++) {
            double x, y;
            do {
                x = (R.nextDouble() * (width + 400d)) - 200d;
                y = (R.nextDouble() * (height + 400d)) - 200d;
            } while(isCloseToAPoint(x, y));
            points[i] = new Point_dt(x, y);
        }
    }
    
    private void triangulate() {
        triangulation = new Delaunay_Triangulation(points);
        triangles = new Triangle_dt[triangulation.trianglesSize()];
        
        int i = 0;
        Iterator trianglesIterator = triangulation.trianglesIterator();
        while(trianglesIterator.hasNext()) {
            triangles[i] = (Triangle_dt)trianglesIterator.next();
            i++;
        }
        
        renderedTriangles = new Triangle[triangles.length];
        for(int  j = 0; j < renderedTriangles.length; j++) {
            int alpha = R.nextInt(75);
            int colorIndex = R.nextInt(2);
            Triangle_dt t = triangles[j];
            if(!t.isHalfplane()) {
                Polygon triangle = new Polygon();
                triangle.addPoint((int)t.p1().x(), (int)t.p1().y());
                triangle.addPoint((int)t.p2().x(), (int)t.p2().y());
                triangle.addPoint((int)t.p3().x(), (int)t.p3().y());
                renderedTriangles[j] = new Triangle(triangle, globalColors[colorIndex], alpha);
            }
        }
    }
    
    private void generateColors() {
        float h = R.nextFloat(); //Random hue (between 0 and 360 degrees)
        float h2 = h + 0.5f; //Inverted color (+ 180 degrees)
        float s = 0.75f;//R.nextFloat() * 0.15f + 0.6f; //Random saturation with a minimal value of 0.6f and a maximal value of 0.75f
        float b = 0.75f;//R.nextFloat() * 0.25f + 0.75f; //random brightness with a minimal value of 0.75f
        
        int hsbColor = Color.HSBtoRGB(h, s, b);
        Color c = new Color(hsbColor);
        globalColors[0] = c;
        
        hsbColor = Color.HSBtoRGB(h2, s, b);
        c = new Color(hsbColor);
        globalColors[1] = c;
    }
    
    private boolean isCloseToAPoint(double x, double y) {
        for(Point_dt p : points)
            if(p != null)
                if(Math.abs(x - p.x()) < proximityRange && Math.abs(y - p.y()) < proximityRange)
                    return true;
        return false;
    }
}