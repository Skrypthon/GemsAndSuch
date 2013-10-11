
/** Abstract vector plotter (ab)using CartesianSolarSystem to construct basic shape primitives from circles.
 *
 * Also handles SolarSystem's silly method of closing when it gets to 100k entities by throwing exceptions like a good Java program should.
 */
public class Plotter extends CartesianSolarSystem{

    // Entity limit, used to self-regulate.
    private static int ENTITY_LIMIT = 100000;
    private int entity_count = 0;

    /** The resolution at which to sample images when overplotting to the underlying solarsystem.
     * Low values may cause EntityLimitExceptions
     */
    private static double VECTOR_PLOT_RESOLUTION = 0.5;

    /** Create a new plotter window with a given title. */
    public Plotter(int width, int height, String title){
        super(width, height);
        
		this.setTitle(title);
    }

  
    /** Returns the active entity count. */
    public int getEntityCount(){
        return entity_count;
    }

    /** Plot at a circle.
     *
     * @x The x co-ordinate of the circle
     * @y The y co-ordinate of the circle
     * @r The radius of the circle
     * @col A string representing the colour.
     *
     * @see SolarSystem
     */
    public void circle(double x, double y, double r, String col) throws EntityLimitException{
        checkEntities();
        super.drawSolarObject(x, y, r, col);
        entity_count += 1;
    }

    /** Plot a circle with an outline of a given stroke width.  Renders by overplotting two circles, so is fairly entity-cheap.
     */
    public void circleOutline(double x, double y, double maxr, double lineWidth, String col, String colOutline) throws EntityLimitException{
        checkEntities();
        super.drawSolarObject(x, y, maxr, colOutline);
        super.drawSolarObject(x, y, maxr - lineWidth, col);
        entity_count += 2;
    }

    /** Plot a single pixel.  In reality this is a circle of diamater 1.
     */
    public void pixel(double x, double y, String col) throws EntityLimitException{
        checkEntities();
        super.drawSolarObject(x, y, 1, col);
        entity_count += 1;
    }

    /** Plot a horizontal line with a given stroke thickness.  Works by strategically placing circles.  It's been adapted to be as efficient as possible and responds to the VECTOR_PLOT_RESOLUTION, but still is costly.
     */
    public void hline(double x, double y, double length, double lineWidth, String fg) throws EntityLimitException{
    
        // Pick a suitable distance between circles
        double gap = Math.max(lineWidth / 4.0, VECTOR_PLOT_RESOLUTION);

        for(double i=x; i<x+length; i+=gap)
            circle(i, y, lineWidth, fg);
    }


    /** Plot a vertical line with a given stroke thickness.  As with hline, it uses circles.
     */
    public void vline(double x, double y, double height, double lineWidth, String fg) throws EntityLimitException{
    
        // Pick a suitable distance between circles
        double gap = Math.max(lineWidth / 4.0, VECTOR_PLOT_RESOLUTION);

        for(double j=y; j<y+height; j+=gap)
            circle(x, j, lineWidth, fg);

    }

    /** Draw a hollow box with a given stroke width.  Uses many lines, and is thus entity-expensive.
     */
    public void box(double x, double y, double width, double height, double lineWidth, String fg) throws EntityLimitException{
        hline(x, y, width, lineWidth, fg);
        vline(x, y, height, lineWidth, fg);
        vline(x + height, y, width, lineWidth, fg);
        hline(x, y + width, height, lineWidth, fg);
    }


    /** Render an ASCII-character to the screen, using a given font, with a given background colour.  As this 
     * uses a lot of pixel plotting it is very expensive.
     *
     * @param bg Set to "" to use transparent background.
     */
    public void character(char c, double x, double y, Font f, String fg, String bg) throws EntityLimitException{
        for(double i=0; i<f.getCharWidth(); i+=VECTOR_PLOT_RESOLUTION){
            for(double j=0; j<f.getCharHeight(); j+=VECTOR_PLOT_RESOLUTION){

                if(f.sampleChar(c, (int)i, (int)j))
                    pixel(x + i, y + j, fg);
                else if(!bg.equals(""))
                    pixel(x + i, y + j, bg);
            }
        }
    }
    
   
    /** Render a string to the screen.  Even more catastrophically expensive than character, especially if the VECTOR_PLOT_RESOLUTION
     * is high.
     */
    public void string(String s, double x, double y, Font f, String fg, String bg) throws EntityLimitException{

        for(int i = 0; i<s.length(); i++){
            character(s.charAt(i), x + i*f.getCharWidth(), y, f, fg, bg);
        }

    }

    /** Delays for 10 ms and then clears the screen.
     *
     * Also resets internal entity counter.
     */
    public void delayAndClear(){
        super.finishedDrawing();
        entity_count = 0;
    }

    /** Test the entity limit and throw exceptions if hit. */
    private void checkEntities() throws EntityLimitException{
        if(entity_count >= ENTITY_LIMIT){
            throw new EntityLimitException();
        }
    }

    /** Thrown when too many objects are rendered for a given scene. */
    public class EntityLimitException extends Exception{
    }

}
