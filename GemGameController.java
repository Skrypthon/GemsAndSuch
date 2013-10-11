
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.MouseInfo;

public class GemGameController implements MouseListener, MouseMotionListener{

    private GemGame g;
    private GemRenderer r;

    private boolean continueGame = true;

    private int GEM_BOX_X = 200;
    private int GEM_BOX_Y = 20;

    private int GEM_SIZE = 50;

    private long GAME_LENGTH_IN_TICKS = 8000;

    // Somewhere to keep mouse actions
    private ArrayList<ClickArea> buttons = new ArrayList<ClickArea>();

    public GemGameController(GemRenderer r, GemGame g){
        this.r = r;
        this.g = g;

        setupClickAreas();

        gameLoop();
    }


    /** Set up click areas for the UI. */
    public void setupClickAreas(){

        // Gem click areas
        ClickArea cl;
        for(int i=0; i<g.GRID_WIDTH; i++){
            for(int j=0; j<g.GRID_WIDTH; j++){

                cl = new GemClickArea(g, i, j,
                        GEM_BOX_X + (GEM_SIZE * i),
                        GEM_BOX_Y + (GEM_SIZE * j),
                        GEM_SIZE,
                        GEM_SIZE
                        );

                buttons.add(cl);
            }
        }
    }

    public void gameLoop(){

        r.addMouseListener(this);
        r.addMouseMotionListener(this);

        long ticksRemaining = GAME_LENGTH_IN_TICKS;
        while(ticksRemaining > 0){

            drawBoilerplate(ticksRemaining);
            renderGems();

            /* System.out.println("DEBUG: Entities: " + r.getEntityCount()); */

            r.delayAndClear();
            ticksRemaining -= 1;
        }


        r.removeMouseListener(this);
        r.removeMouseMotionListener(this);
    }


    private void drawBoilerplate(long ticksRemaining){
        try{

            // Line down the LHS
            r.vline(180, 0, r.getHeight(), 2, "BLUE");

            r.string("Gems!", 10, 10, r.fontBig, "WHITE", "");
            r.hline(10, 50, 160, 5, "BLUE");
            r.string("Score: ", 10, 60, r.fontSmall, "WHITE", "");
            r.string("" + g.getScore(), 10, 80, r.fontBig, "YELLOW", "");
            r.string("Time: ", 10, 130, r.fontSmall, "WHITE", "");
            r.string("" + ticksRemaining, 10, 160, r.fontBig, "YELLOW", "");
            r.hline(10, 200, (160.0/GAME_LENGTH_IN_TICKS) * ticksRemaining, 5, "YELLOW");
            
            r.string("Combo: ", 10, 230, r.fontSmall, "WHITE", "");
            r.string("" + g.lastCombo(), 10, 260, r.fontBig, "YELLOW", "");

            r.box(GEM_BOX_X, GEM_BOX_Y, g.GRID_WIDTH * GEM_SIZE, g.GRID_WIDTH * GEM_SIZE, 1, "GREY");

        }catch(Plotter.EntityLimitException e){
        }
    }

    private void renderGems(){
        try{
            for(int i=0; i<g.GRID_WIDTH; i++){
                for(int j=0; j<g.GRID_WIDTH; j++){
                    renderGem(g.getGemXY(i, j),
                            GEM_BOX_X + (GEM_SIZE * i),
                            GEM_BOX_Y + (GEM_SIZE * j)
                            );
                }
            }
        }catch(Plotter.EntityLimitException e){
            System.err.println("Error!  Can't render all the gems!");
        }
    }


    /* Render a single gem in the GEM_SIZExGEM_SIZE space with the top-left at x, y. */
    private void renderGem(Gem gem, double x, double y) throws Plotter.EntityLimitException{

        // Outline on hover using painter's algorithm 
        if(gem.isHover()){
            r.circle(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE , "WHITE");
        }
        
        // Gem rendering.  TODO: make this much much fancier.
        switch(gem.type){
            case 0: r.circle(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE / 1.2, "BLUE");
                    break;
            case 1: r.circle(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE / 1.2, "RED");
                    break;
            case 2: r.circle(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE / 1.2, "GREEN");
                    break;
            case 3: r.circle(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE / 1.2, "MAGENTA");
                    break;
            case 4: r.circle(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE / 1.2, "GREY");
                    break;
            case 5: r.circle(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE / 1.2, "CYAN");
                    break;
            default: r.circleOutline(x + GEM_SIZE / 2.0, y + GEM_SIZE / 2.0, GEM_SIZE / 1.2, 2, "BLACK", "WHITE");
                    break;
        }


        // TODO: Retricule when active
        if(gem.isActive()){

            // FIXME: this presumes that GEM_SIZE is 50.
            r.hline(x, y, 10, 2, "YELLOW");
            r.hline(x+40, y, 10, 2, "YELLOW");
            r.hline(x, y+50, 10, 2, "YELLOW");
            r.hline(x+40, y+50, 10, 2, "YELLOW");
            
            r.vline(x, y, 10, 2, "YELLOW");
            r.vline(x, y+40, 10, 2, "YELLOW");
            r.vline(x+50, y, 10, 2, "YELLOW");
            r.vline(x+50, y+40, 10, 2, "YELLOW");
        }

    }

    private void checkButtonHoverStates(){
     }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        for(ClickArea cl: buttons){
            cl.click(r.adjustXForInsets(e.getX()), r.adjustYForInsets(e.getY()));
        }
    }


    public void mouseDragged(MouseEvent e){
    }

    // Each time the mouse moves, handle hover states for the various buttons.
    public void mouseMoved(MouseEvent e){
        // Iterate over the buttons
        for(ClickArea cl: buttons){
          
            if(cl.isInBounds(r.adjustXForInsets(e.getX()), r.adjustYForInsets(e.getY()))){
                if(!cl.getHoverState()){
                    // In bounds but not yet hovering: fire "in" event
                    cl.setHoverState(true);
                    cl.hoverin();
                }
            }else{
                if(cl.getHoverState()){
                    // Out of bounds but hovering, fire "out" event.
                    cl.setHoverState(false);
                    cl.hoverout();
                }
            }
        }

    }

}
