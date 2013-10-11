

import java.awt.event.*;

import java.io.IOException;

public class GemRenderer extends Plotter{

    public Font fontSmall;
    public Font fontBig;

    public GemRenderer(int width, int height, String title) throws IOException{
        super(width, height, title);

        setResizable(false);

        fontSmall   = new Font("font.gif",     16, 16, 16, 16 );
        fontBig     = new Font("fontbig.gif",  16, 16, 32, 32 );
    }

 
}
