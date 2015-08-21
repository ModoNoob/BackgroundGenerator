package net.modonoob.backgroundgenerator;

import net.modonoob.backgroundgenerator.generator.BackgroundGenerator;
import net.modonoob.backgroundgenerator.triangle.Triangle;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class BackgroundRenderer extends BasicGame {
    private static final int width = 800;
    private static final int height = 600;
    
    private BackgroundGenerator bg;
    private Triangle[] renderedTriangles;
    
    public BackgroundRenderer(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        bg = new BackgroundGenerator(width, height);
        renderedTriangles = bg.generate();
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
        
        /*GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);*/
        GL11.glEnable(GL13.GL_MULTISAMPLE);
    }

    @Override
    public void update(GameContainer gc, int d) throws SlickException {
        Input input = gc.getInput();
        
        if(input.isKeyDown(Keyboard.KEY_R)) {
            renderedTriangles = bg.generate();
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setBackground(Color.white);
        g.setLineWidth(2);
        for(Triangle t : renderedTriangles) {
            g.setColor(t.color);
            g.fill(t.triangle);
        }
        
        /*g.setColor(Color.red);
        for(Point_dt p : points)
            g.fillRect((float)p.x(), (float)p.y(), 4, 4);*/
    }
    
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new BackgroundRenderer("Background Generator Preview"));
        app.setDisplayMode(width, height, false);
        app.setShowFPS(false);
        //app.setTargetFrameRate(5);
  
        //System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        app.start();
    }
}
