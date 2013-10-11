
import java.awt.event.*;

/** Presents a nice game summary page to the player. */
public class GemSummaryPage implements MouseListener{

    // Keep track of resources, and state for if we've been told to quit
    private GemRenderer r;
    private GemGame g;
    private boolean clicked = false;

    /** Display a summary page of a given game, using a given renderer. */
    public GemSummaryPage(GemRenderer r, GemGame g){
        
        this.r = r;
        this.g = g;
        r.addMouseListener(this);

        displayTitle();

        r.delayAndClear();
    }


    /** Display the summary page and wait. */
    public void displayTitle(){
        try{

            while(!this.clicked){

                r.niceBackground();

                // Title
                r.string("Game Over", 80, 50, r.fontBig, "WHITE", "");

                // Line
                r.hline(10, 90, 580, 5, "BLUE");
                r.hline(10, 90, 580, 3, "GREY");

                // Score readout
                r.string("Your score was:", 200, 250, r.fontSmall, "WHITE", "");
                r.string("" + g.getScore(), 230, 280, r.fontBig, "YELLOW", "");


                // Click to begin
                r.string("Click to quit", 200, 350, r.fontSmall, "WHITE", "");

                r.delayAndClear();
            }

        }catch(Plotter.EntityLimitException e){
            System.out.println("Entity limit reached...");
        }

    }


    /** Tells the rendering loop to stop waiting and quit. */
    public void mouseReleased(MouseEvent e) {
        r.removeMouseListener(this);
        System.exit(0);
    }

    /* Java interface cruft. */
    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e) {    }

}

