
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/** Handles a bitmap so that it may be sampled for font rendering. */
public class Font{

    /** The original image to use. */
    BufferedImage img;

    /** Number of tiles high/wide for source img */
    private int ntileswidth;
    private int ntilesheight;

    /** Width of each tile */
    private int tilew;
    private int tileh;

    /** Create a new Font from a given image and a description of the tile pattern.
     *
     * @param filename The image to load.
     * @param ntileswidth The number of tiles wide the grid is (assumed horizontal, ASCII)
     * @param ntilesheight The number of tiles high the grid is (used for error checking only)
     * @param tilew The width of each individual tile, in pixels
     * @param tileh The height of each individual tile, in pixels
     */
    public Font(String filename, int ntileswidth, int ntilesheight, int tilew, int tileh) throws java.io.IOException{

        img = ImageIO.read(new java.io.File(filename));

        this.tilew = tilew;
        this.tileh = tileh;

        this.ntileswidth = ntileswidth;
        this.ntilesheight = ntilesheight;
    }

    /** Returns the width, in pixels, of each charcter. */
    public int getCharWidth(){
        return tilew;
    }

    /** Return the height, in pixels, of each individual character. */
    public int getCharHeight(){
        return tileh;
    }

    /** Used by the renderer to sample pixels for conversion to a different plane.
     *
     * @param c The ASCII character to sample
     * @param xoff X offset from 0,0 in top left down to getCharWidth(), getCharHeight()
     * @param xoff Y offset from 0,0 in top left down to getCharWidth(), getCharHeight()
     */
    public boolean sampleChar(char c, int xoff, int yoff){
        int asciiValue = (int) c;

        // Tile grid co-ordinate
        int row     = (int)Math.floor( asciiValue / ntileswidth );
        int column  = asciiValue % ntileswidth;

        /* System.out.println("Char: " + c + " row: " + row + " col: " + column); */

        // Top-left pixel co-ordinates
        int xoffset = column * tilew;
        int yoffset = row * tileh;
       
        return ((img.getRGB(xoffset + xoff, yoffset + yoff) & 0x1) != 0);
    }


}

