package net.modonoob.backgroundgenerator;

import java.io.File;
import java.util.Date;
import net.modonoob.backgroundgenerator.generator.BackgroundGenerator;
import net.modonoob.backgroundgenerator.triangle.Triangle;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;

public class BackgroundCreator extends BasicGame {
    private static final String DIRECTORY = "Renders/";
    
    private static int width, height;
    private static BackgroundGenerator bg;
    private static Triangle[] triangles;
    
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new BackgroundCreator("Background Generator"));
        app.setDisplayMode(0, 0, false);
        
        app.start();
    }
    
    private static void createImage() {
        Image image = null;
        Graphics imageGraphics = null;
        try {
            image = new Image(width, height);
            imageGraphics = image.getGraphics();
        } catch(SlickException e) {
            System.out.println("Oops, something went wrong while creating the image... Let's hope this dosen't happen again!");
        }
        imageGraphics.setBackground(Color.white);
        
        for(Triangle t : triangles) {
            imageGraphics.setColor(t.color);
            imageGraphics.fill(t.triangle);
        }
        
        imageGraphics.flush();
        
        Date d = new Date();
        
        try {
            ImageOut.write(image, DIRECTORY + d.getTime() + ".png");
        } catch(SlickException e) {
            System.out.println("Could not write image file to disk " + DIRECTORY + d.getTime() + ".png");
        }
        
        System.out.println("Created image " + DIRECTORY + d.getTime() + ".png");
    }
    
    private static void createRendersFolder() {
        File d = new File(DIRECTORY);
        if(!d.exists()) {
            d.mkdir();
            System.out.println("Created folder " + d.getAbsolutePath());
        }
    }

    public BackgroundCreator(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        System.out.println("Initializing BackgroundGenerator...");
        width = 1920 * 2;
        height = 1080;
        bg = new BackgroundGenerator(width, height);
        triangles = bg.generate();
        System.out.println("Creating images...");
        for(int i = 0; i < 1; i++) {
            triangles = bg.generate();
            createImage();
        }
        System.exit(0);
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {
        
    }
}
