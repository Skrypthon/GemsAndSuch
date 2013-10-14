

import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

/** Handles Gem-game-specific rendering tasks that are common to the whole look and feel. */
public class GemRenderer extends Plotter{

    /** Small font used in game. */
    public Font fontSmall;

    /** Large font used in game. */
    public Font fontBig;

    // Background circles
    private ArrayList<Point2D.Double> bgcircles = new ArrayList<Point2D.Double>();
    private ArrayList<Point2D.Double> bgmotion = new ArrayList<Point2D.Double>();

    // Store ms since last frame
    private long lastFrameMillis = System.currentTimeMillis();
    private double fps = 1;

    // Size of a gem
    public static final int GEM_SIZE = 50;

    /** Create a new rendering window and context, with a given title.
     *
     * @throws IOException when the font files cannot be loaded properly.
     */
    public GemRenderer(int width, int height, String title) throws IOException{
        super(width, height, title);

        // Set unresizable
        setResizable(false);

        // Load fonts
        fontSmall   = new Font("font.gif",     16, 16, 16, 16 );
        fontBig     = new Font("fontbig.gif",  16, 16, 32, 32 );

        // Load background circles
        for(int i=0; i<100; i++){
            double xpos = Math.random() * getWidth();
            double ypos = Math.random() * getHeight();
            bgcircles.add(new Point2D.Double(xpos, ypos));
            
            double dx = Math.random() - 0.5;
            double dy = Math.random() - 0.5;
            bgmotion.add(new Point2D.Double(dx, dy));
        }
    }


    /** Render a nice background of floating circles. 
     * 
     * This should be called regularly to ensure the motion looks all nice and such.
     *
     * */
    public void niceBackground() throws EntityLimitException{
        for(int i=0; i<bgcircles.size(); i++){

            // Load from parallel arraylists
            Point2D.Double p = bgcircles.get(i);
            Point2D.Double motion = bgmotion.get(i);

            // Move the circles so they drift
            p.setLocation( p.x + motion.x, p.y + motion.y );

            // Render, with some size variation
            circleOutline(p.x, p.y, 50 + (i * 2), 2.0, "BLACK", "DARKGREY");
        }

    }

    /** Returns the FPS, computed between delayAndClear.
     * Max FPS is 100 due to the delay in SolarSystem.
     */
    public double getFPS(){
        return fps;
    }

    /** Delay and clear the screen.  Called to finalise a frame,
     * also computes FPS.
     */
    public void delayAndClear(){

        // Compute FPS
        long now = System.currentTimeMillis();
        fps = (1000.0 / (now - lastFrameMillis));
        lastFrameMillis = now;

        // And call super
        super.delayAndClear();
        lastFrameMillis = System.currentTimeMillis();
    }


    /** Render the gems in a grid. */
    public void renderGems(RenderGrid grid, GemGame g) throws Plotter.EntityLimitException{
        for(int i=0; i<grid.getGrid().length; i++){
            if(grid.getGrid()[i] != null)
                renderGem(grid.getGrid()[i], g);
        }
    }

    /** Render a single gem in the GEM_SIZExGEM_SIZE space with the top-left at x, y. */
    public void renderGem(RenderGem rgem, GemGame g) throws Plotter.EntityLimitException{

        // Read render positions from the gem
        double x = rgem.getScreenX();
        double y = rgem.getScreenY();
        double visibility = rgem.getVisibility();

        // TODO: Render fade in/out with more than 1 bit of accuracy
        if(visibility < 0.999)
            return;

        // Outline on hover using painter's algorithm 
        if(rgem.isHover()){
            circleOutline(x + 50 / 2.0, y + 50 / 2.0, 50 * rgem.getScale(), 5, "WHITE", "GREY");
        }
        
        // Gem rendering.  TODO: make this much much fancier.
        switch(rgem.gem.type){
            case 0: 
                circle(x + 25, y + 25, 42 * rgem.getScale(), "ORANGE");

                // Orange is a clock
                double angle = System.currentTimeMillis() % 10000 / (5000.0/Math.PI);
                double xdev = 20 * Math.cos(angle);
                double ydev = 20 * Math.sin(angle);
                circleOutline(x + 25 + xdev, y + 25 + ydev, 8 * rgem.getScale(), 2, "BLACK", "RED");
                /* circleOutline(x + 15, y + 15, 7, 1, "WHITE", "RED"); */
                    break;
            case 1: 
                    circleOutline(x + 25, y + 25, 42 * rgem.getScale(), 2, "ORANGE", "RED");
                    circleOutline(x + 25, y + 25, 32 * rgem.getScale(), 2, "BLUE", "RED");
                    circleOutline(x + 25, y + 25, 22 * rgem.getScale(), 2, "GRAY", "RED");
                    circleOutline(x + 25, y + 25, 12 * rgem.getScale(), 2, "MAGENTA", "RED");
                    circleOutline(x + 25, y + 25, 2 * rgem.getScale(), 2, "BLACK", "RED");
                    break;
            case 2: 
                    circle(x + 25, y + 25, 42, "GREEN");
                    circle(x + 35, y + 25, 20, "BLUE");
                    circle(x + 35, y + 25, 10, "GREEN");
                    circle(x + 25, y + 15, 20, "BLUE");
                    circle(x + 25, y + 15, 10, "GREEN");
                    circle(x + 25, y + 35, 20, "BLUE");
                    circle(x + 25, y + 35, 10, "GREEN");
                    circle(x + 15, y + 25, 20, "BLUE");
                    circle(x + 15, y + 25, 10, "GREEN");
                    circle(x + 25, y + 25, 20, "GREEN");
                    circleOutline(x + 25, y + 25, 20, 8, "GREEN", "BLUE");
                    break;
            case 3: 
                    circle(x + 25, y + 25, 42 * rgem.getScale() , "MAGENTA");
                    vline(x + 25, y + 4 , 42 * rgem.getScale(), 2, "YELLOW");
                    vline(x + 15, y + 8 , 36 * rgem.getScale(), 2, "YELLOW");
                    vline(x + 35, y + 8 , 36 * rgem.getScale(), 2, "YELLOW");
                    hline(x + 4, y + 25 , 42 * rgem.getScale(), 2, "YELLOW");
                    hline(x + 8, y + 15 , 36 * rgem.getScale(), 2, "YELLOW");
                    hline(x + 8, y + 35 , 36 * rgem.getScale(), 2, "YELLOW");
                    break;
            case 4: 
                    circle(x + 25, y + 25, 42 * rgem.getScale(), "RED");
                    for(int i=0; i<5; i++){
                        circle(x + 25 + Math.random() * 20 * rgem.getScale() - 10, 
                               y + 25 + Math.random() * 20 * rgem.getScale() - 10, 2, "ORANGE");
                    }
                    break;
            case 5: 
                    circle(x + 25, y + 25, 42 * rgem.getScale(), "CYAN");
                    // The cyan one shows the last score with the size of its inner circle,
                    // up to a max score of 42
                    circleOutline(x + 25, y + 25, Math.min(g.lastCombo(), 42) * rgem.getScale(), 4, "CYAN", "BLUE");
                    

                    break;
            default: circleOutline(x + 50 / 2.0, y + 50 / 2.0, 50 / 1.2, 2, "BLACK", "WHITE");
                    break;
        }


        // Draw the reticule when a gem is active
        if(rgem.gem.isActive()){
            hline(x, y, 10, 2, "YELLOW");
            hline(x+40, y, 10, 2, "YELLOW");
            hline(x, y+50, 10, 2, "YELLOW");
            hline(x+40, y+50, 10, 2, "YELLOW");
            
            vline(x, y, 10, 2, "YELLOW");
            vline(x, y+40, 10, 2, "YELLOW");
            vline(x+50, y, 10, 2, "YELLOW");
            vline(x+50, y+40, 10, 2, "YELLOW");
        }

    }


}
