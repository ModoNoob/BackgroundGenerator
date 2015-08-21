package net.modonoob.backgroundgenerator.generator;

import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Point_dt;
import delaunay_triangulation.Triangle_dt;
import java.util.Iterator;
import java.util.Random;
import net.modonoob.backgroundgenerator.triangle.Triangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;

public class BackgroundGenerator {
    private final int nbOfPoints = 100;
    private final int proximityRange = 50;
    
    private static int width;
    private static int height;
    
    private Point_dt[] points;
    
    private Triangle_dt[] triangles;
    private Triangle[] renderedTriangles;
    
    private Color[] globalColors;
    
    private Delaunay_Triangulation triangulation;
    
    private Random R;
    
    public BackgroundGenerator(int w, int h) {
        width = w;
        height = h;
        
        points = new Point_dt[nbOfPoints];
        globalColors = new Color[2];
        R = new Random();
    }
    
    public void init() {
        
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
            int alpha = R.nextInt(125);
            int colorIndex = R.nextInt(2);
            Triangle_dt t = triangles[j];
            float[] ps = new float[6];
            ps[0] = (float)t.p1().x();
            ps[1] = (float)t.p1().y();
            ps[2] = (float)t.p2().x();
            ps[3] = (float)t.p2().y();
            if(t.isHalfplane()) {
                //System.out.println("I am a half plane: (" + ps[0] + ", " + ps[1] + "), (" + ps[2] + ", " + ps[3] + ")");
                renderedTriangles[j] = new Triangle(new Line(ps[0], ps[1], ps[2], ps[3]), new Color(0, 0, 0, 0), alpha);
            }
            if(!t.isHalfplane()) {
                ps[4] = (float)t.p3().x();
                ps[5] = (float)t.p3().y();
                //System.out.println("I am NOT a half plane: (" + ps[0] + ", " + ps[1] + "), (" + ps[2] + ", " + ps[3] + "), (" + ps[4] + ", " + ps[5] + ")");
                renderedTriangles[j] = new Triangle(new Polygon(ps), globalColors[colorIndex], alpha);
            }
        }
    }
    
    private void generateColors() {
        float h = R.nextFloat();
        float h2 = h - Math.min(R.nextFloat() + 0.25f, 0.75f);
        //float h3 = h + 0.5f;
        float s = 0.80f;
        float l = 0.8f;
        
        int color = java.awt.Color.HSBtoRGB(h, s, l);
        java.awt.Color c = new java.awt.Color(color);
        
        globalColors[0] = new Color(c.getRed(), c.getGreen(), c.getBlue());
        
        color = java.awt.Color.HSBtoRGB(h2, s, l);
        c = new java.awt.Color(color);
        
        globalColors[1] = new Color(c.getRed(), c.getGreen(), c.getBlue());
        
        /*color = java.awt.Color.HSBtoRGB(h3, s, l);
        c = new java.awt.Color(color);
        
        globalColors[2] = new Color(c.getRed(), c.getGreen(), c.getBlue());*/
    }
    
    private boolean isCloseToAPoint(double x, double y) {
        for(Point_dt p : points) {
            //System.out.println("X: " + p.getX() + ", x: " + x + "\nY: " + p.getY() + ", y: " + y);
            if(p != null)
                if(Math.abs(x - p.x()) < proximityRange && Math.abs(y - p.y()) < proximityRange)
                    return true;
        }
        return false;
    }
}