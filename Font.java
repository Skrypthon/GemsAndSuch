
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Font{

    BufferedImage img;

    /* Number of tiles high/wide for source img */
    private int ntileswidth;
    private int ntilesheight;

    /* Width of each tile */
    private int tilew;
    private int tileh;

    public Font(String filename, int ntileswidth, int ntilesheight, int tilew, int tileh) throws java.io.IOException{

        img = ImageIO.read(new java.io.File(filename));

        this.tilew = tilew;
        this.tileh = tileh;

        this.ntileswidth = ntileswidth;
        this.ntilesheight = ntilesheight;
    }

    public int getCharWidth(){
        return tilew;
    }

    public int getCharHeight(){
        return tileh;
    }

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

