
// text renderer libs
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Test{

    public static void main(String[] args){

        try{


            // Instantiate
            GemRenderer r = new GemRenderer(640, 480, "Gems and Such");


            // Display title page
            new GemTitlePage(r);

            System.out.println("Clicked.  Moving to new game...");

            GemGame game = new GemGame();

            new GemGameController(r, game);

            new GemSummaryPage(r, game);

        }catch(java.io.IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        /* test(); */
    }


    private static void test(){
        try{



            



/*        SolarSystem ss = new SolarSystem( 500, 500 );
        ss.drawSolarObject(0, 0, 100, "BLUE");
        /* ss.finishedDrawing();  * /
*/
        
        /* CartesianSolarSystem cs = new CartesianSolarSystem( 500, 500 ); */
        /* cs.drawSolarObject(0, 0, 100, "RED"); */
        /* cs.drawSolarObject(10, 0, 100, "RED"); */
        /* cs.drawSolarObject(20, 0, 100, "RED"); */

       

        /* cs.finishedDrawing(); */
        Plotter r = new Plotter( 500, 500, "Low-level plotter" );
        r.pixel(300, 40, "YELLOW");
        r.circle(100, 100, 100, "RED");
        /* r.circleOutline(r.centreX(), r.centreY(), 100, 2, "GREEN", "BLUE"); */

 
        /* int x = 0; */
        /* while(true){ */
        /*     for(x = 0; x<r.getWidth(); x+=2){ */
        /*          */
        /*         r.drawSolarObject(x, x, 100, "RED"); */
        /*         r.delayAndClear(); */
        /*     } */
        /* } */



        r.hline(100, 100, 100, 5, "MAGENTA");
        r.vline(100, 100, 100, 5, "MAGENTA");

        
        r.box(200, 200, 10, 10, 2, "CYAN");
        

        Font f;
        try{
            /* f = new Font("font.gif",  16, 16, 16, 16 ); */
            f = new Font("fontbig.gif",  32, 32, 32, 32 );


            r.character('c', 10, 10, f, "WHITE", "BLACK");
            r.string("abcdefghj", 10, 40, f, "WHITE", "");

        }catch(java.io.IOException e){
            e.printStackTrace();
            System.exit(1);
        }



        }catch(Plotter.EntityLimitException e){
            System.out.println("Entity limit reached...");
        }

    }

}
