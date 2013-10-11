
import java.awt.event.*;

public class GemSummaryPage implements MouseListener{

    private GemRenderer r;
    private GemGame g;
    private boolean clicked = false;

    public GemSummaryPage(GemRenderer r, GemGame g){
        
        this.r = r;
        this.g = g;
        r.addMouseListener(this);

        displayTitle();

        r.delayAndClear();
    }


    // Title Page
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

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        r.removeMouseListener(this);
        System.exit(0);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

}

