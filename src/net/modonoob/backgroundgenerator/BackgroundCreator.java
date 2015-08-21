package net.modonoob.backgroundgenerator;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import net.modonoob.backgroundgenerator.generator.BackgroundGenerator;
import net.modonoob.backgroundgenerator.triangle.Triangle;

public class BackgroundCreator {
    private static final String DIRECTORY = "Renders/";
    
    private static int width, height, imageQty;
    private static BackgroundGenerator bg;
    private static Triangle[] triangles;
    
    public static void main(String[] args) {
        createRendersFolder();
        init();
    }
    
    private static void createImage() {
        BufferedImage image;
        Graphics2D imageGraphics;
       
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        imageGraphics = image.createGraphics();
        
        imageGraphics.setStroke(new BasicStroke(5));
        
        for(Triangle t : triangles) { //First, you draw the lines of the polygons.
            if(t != null) {
                imageGraphics.setColor(t.color);
                imageGraphics.drawPolygon(t.triangle);
            }
        }
        
        imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(Triangle t : triangles) { //Then, you draw the actual polygons over the lines so that the AWT antialiasing method knows what colors to use instead of going for white.
            if(t != null) {
                imageGraphics.setColor(t.color);
                imageGraphics.fillPolygon(t.triangle);
            }
        }
        
        Date d = new Date();
        
        File outputFile = null;
        try {
            outputFile = new File(DIRECTORY + d.getTime() + ".png");
            ImageIO.write(image, "png", outputFile);
            
            System.out.println("\tWriting image file \"" + outputFile.getName() + "\" to disk...");
        } catch(IOException e) {
            System.out.println("\tCould not write image file to disk " + outputFile.getName());
        }
        
        System.out.println("\tCreated image " + DIRECTORY + d.getTime() + ".png");
    }
    
    private static void createRendersFolder() {
        File d = new File(DIRECTORY);
        if(!d.exists()) {
            d.mkdir();
            System.out.println("Created folder " + d.getAbsolutePath());
        }
    }

    public static void init() {
        System.out.println("Initializing BackgroundGenerator...");
        width = 1920 * 2;
        height = 1080;
        imageQty = 5;
        bg = new BackgroundGenerator(width, height);
        for(int i = 1; i <= imageQty; i++) {
            System.out.println("Creating image " + i + " of " + imageQty + "...");
            triangles = bg.generate();
            createImage();
        }
        System.exit(0);
    }
}
