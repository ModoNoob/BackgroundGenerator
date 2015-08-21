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
        //Example usage: java -jar BackgroundGenerator 1920 1080 5 Renders/
        if(args.length > 4 || args.length < 4) {
            System.out.println("Needed arguments: [desktop width] [desktop height] [number of image to generate] [output directory]");
            System.out.println("For desktop width and height, you can specify something like \"1920x2\" if you have two horizontal monitors.");
            System.exit(0);
        }
        
        System.out.println("Initializing BackgroundGenerator...");
        width = parseSize(args[0]);
        height = parseSize(args[1]);
        imageQty = Integer.parseInt(args[2]);
        System.out.println("Width: " + width + ", Height: " + height + ", Number of images to generate: " + imageQty);
        bg = new BackgroundGenerator(width, height);
        
        createRendersFolder(args[3]);
        
        for(int i = 1; i <= imageQty; i++) {
            System.out.println("Creating image " + i + " of " + imageQty + "...");
            triangles = bg.generate();
            createImage();
        }
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
    
    private static void createRendersFolder(String path) {
        File d = new File(path);
        if(!d.exists()) {
            d.mkdir();
            System.out.println("Created folder " + d.getAbsolutePath());
        }
    }
    
    private static int parseSize(String s) {
        if(s.contains("x")) {
            String splittedString[] = s.split("x");
            int size = Integer.parseInt(splittedString[0]);
            int qty = Integer.parseInt(splittedString[1]);
            return size * qty;
        }
        return Integer.parseInt(s);
    }
}
