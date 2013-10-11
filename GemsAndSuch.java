
// text renderer libs
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class GemsAndSuch{

    public static void main(String[] args){

        try{

            // Instantiate a shiny new renderer.
            GemRenderer r = new GemRenderer(640, 480, "Gems and Such");

            // Display title page
            new GemTitlePage(r);

            // Create a new game and enter the play screen
            GemGame game = new GemGame();
            new GemGameController(r, game);

            // Produce a summary at the end
            new GemSummaryPage(r, game);

        /* Catch loading errors. */
        }catch(java.io.IOException e){
            System.err.println("Sorry!  Something screwed up, most likely loading the font resources.");
            e.printStackTrace();
            System.exit(1);
        }

    }

}
