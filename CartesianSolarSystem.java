

import java.lang.Math;
import java.awt.Insets;

/** Cartesian version of SolarSystem class.  Also provides adjustments to avoid problems with the inset from the window manager.
 */
public class CartesianSolarSystem extends SolarSystem{

    // used to speed up rad conversions.
    private static double RADIANS_TO_DEGREES = 180.0 / Math.PI;

    /* Store width, height and centre point.
     *
     * NOTE: these are NOT corrected for frame insets, and represent the 
     * SolarSystem renderer's idea of the plane. NOT ours.
     *
     * For the x-y plane limits before transform, see getWidth/getHeight/centreX/centreY.
     */
    protected int width;
    protected int height;
    protected double halfWidth;
    protected double halfHeight;

    // Store frame insets to adjust 0,0 position.
    private Insets is;

    /** Create a new renderer with a given width and height. */
	public CartesianSolarSystem(int width, int height){
        super(width, height);

        // Find the frame insets to make 0,0 actually 0,0
        is = getInsets();

        // This is private in the above classes.  Shadow it.
        this.width  = width;// - is.left - is.right;
        this.height = height;// - is.top - is.bottom;

        // Store this for faster detection later
        this.halfWidth = width/2.0;
        this.halfHeight = height/2.0;
    }

    /** Return the co-ordinate at the centre of the plane. */
    public double centreX(){
        return getHeight()/2.0;
    }

    /** Return the Y co-ordinate at the centre of the plane. */
    public double centreY(){
        return getWidth()/2.0;
    }

    /** Return the width of the rendering plane, taking into account inset restrictions. */
    public int getWidth(){
        return width - is.left - is.right;
    }

    /** Return the height of the rendering plane, taking into account inset restrictions. */
    public int getHeight(){
        return height - is.top - is.bottom;
    }

    /** Adjust a non-inset X coordinate (say, from a mouse event) to an inset one on the plane. */
    public int adjustXForInsets(int x){
        return x - is.left;
    }

    /** Adjust a non-inset Y coordinate (say, from a mouse event) to an inset one on the plane. */
    public int adjustYForInsets(int y){
        return y - is.top;
    }

    /** Render a solar object at the given X, Y co-ordinates. */
	public void drawSolarObject(double x, double y, double diameter, String col){
        
        super.drawSolarObject( 
               getL(x, y), getA(x, y), 
               diameter, col ); 
    }
    
    /** Render a solar object at the given X, Y co-ordinates, relative to another point. */
	public void drawSolarObjectAbout(double xoff, double yoff, 
            double diameter, String col, 
            double x, double y){
        
        super.drawSolarObjectAbout( 
               getL(xoff, yoff), getA(xoff, yoff), 
               diameter, col, 
               getL( x, y ), getA( x, y ) );
    }


    /** Return the angle from origin of an X-Y point. Adjusted such that 0,0 is top left. */
    private double getA(double x, double y){
        if(x == halfWidth && y == halfHeight)
            return 0;

        // Adjust for insets and different origin.
        x += is.left;
        x -= halfWidth;
        y += is.top;
        y -= halfHeight;

        /* System.out.println("X:" + x + ", " + y); */


        // Adjust stuff in the other quadrant
        if(x >= 0)
            y *= -1;
        

        double angle = Math.atan( -y / x ) * RADIANS_TO_DEGREES + 270;

        // Adjust stuff in the other quadrant
        if(x >= 0)
            angle *= -1;
        
        return angle;
    }

    /** Return the distance from origin of an X-Y point. Adjusted such that 0,0 is top left. */
    private double getL(double x, double y){
        if(x == halfWidth && y == halfHeight)
            return 0;

        // Adjust for insets and different origin.
        x += is.left;
        x -= halfWidth;
        y += is.top;
        y -= halfHeight;

        return Math.hypot( x, y );
    }
}

