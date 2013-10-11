

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
 
}
