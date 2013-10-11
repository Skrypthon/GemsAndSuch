
import java.awt.event.*;

/** Displays a title page to the user, and waits for a click to continue. */
public class GemTitlePage implements MouseListener{

    /** Handles to renderer and state for waiting. */
    private GemRenderer r;
    private boolean clicked = false;

    /** Create and display a new title page using the given renderer. */
    public GemTitlePage(GemRenderer r){
        
        this.r = r;
        r.addMouseListener(this);

        displayTitle();

        // Cls afterwards
        r.delayAndClear();
    }


    /** Display the title Page */
    public void displayTitle(){
        try{
            while(!this.clicked){
                r.niceBackground();

                // Title
                r.string("Gems and Such", 80, 50, r.fontBig, "WHITE", "");

                // Line
                r.hline(10, 90, 580, 5, "BLUE");
                r.hline(10, 90, 580, 3, "GREY");

                // Click to begin
                r.string("Click to Begin", 200, 250, r.fontSmall, "WHITE", "");

                r.delayAndClear();
            }
        }catch(Plotter.EntityLimitException e){
            System.out.println("Entity limit reached...");
        }

    }

    /** Tells the interface to stop looping and fall out. */
    public void mouseReleased(MouseEvent e) {
        r.removeMouseListener(this);
        this.clicked = true;
    }

    /* Java interface cruft. */
    public void mousePressed(MouseEvent e) {    }
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e) {    }

}
