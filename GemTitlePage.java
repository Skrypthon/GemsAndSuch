
import java.awt.event.*;

public class GemTitlePage implements MouseListener{

    GemRenderer r;
    private boolean clicked = false;

    public GemTitlePage(GemRenderer r){
        
        this.r = r;
        r.addMouseListener(this);

        displayTitle();

        delayUntilClick();

        r.delayAndClear();
    }


    // Title Page
    public void displayTitle(){
        try{

            // Fancy background
            for(int i=0; i<100; i++){
                double xpos = Math.random() * r.getWidth();
                double ypos = Math.random() * r.getHeight();
                double rad = Math.random() * 200 + 100;

                r.circleOutline(xpos, ypos, rad, 2.0, "BLACK", "DARKGREY");
            }

            // Title
            r.string("Gems and Such", 80, 50, r.fontBig, "WHITE", "");

            // Line
            r.hline(10, 90, 580, 5, "BLUE");
            r.hline(10, 90, 580, 3, "GREY");

            // Click to begin
            r.string("Click to Begin", 200, 250, r.fontSmall, "WHITE", "");

        }catch(Plotter.EntityLimitException e){
            System.out.println("Entity limit reached...");
        }

    }

    private void delayUntilClick(){
        while(!this.clicked){
            try {
                Thread.sleep(100);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        r.removeMouseListener(this);
        this.clicked = true;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

}
