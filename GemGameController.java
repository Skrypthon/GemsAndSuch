
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.MouseInfo;

/** Controls the majority of interaction between UI and game logic.  Handles all input logic. 
 *
 * Bit of a god class really, but hey, MVC is ugly sometimes.
 * */
public class GemGameController implements MouseListener, MouseMotionListener{

    /* Store things we need to get to */
    private GemGame g;
    private GemRenderer r;

    // Gem box location for easier drawing
    private int GEM_BOX_X = 200;
    private int GEM_BOX_Y = 20;

    // How long does a game last before we tell people to bugger off.
    private long GAME_LENGTH_IN_TICKS = 8000;

    // Somewhere to keep mouse actions
    private ArrayList<ClickArea> buttons = new ArrayList<ClickArea>();

    // Used to control pause/quit actions, responds to clicks using the 
    // area system.
    private PauseClickArea pauseGame = new PauseClickArea(this, 30, 360, 120, 20);
    private QuitClickArea quitGame   = new QuitClickArea(this, 30, 390, 120, 20);

    /** Creates an new GemGameController with a given renderer and game object. */
    public GemGameController(GemRenderer r, GemGame g){
        this.r = r;
        this.g = g;

        setupClickAreas();

        gameLoop();
    }


    /** Set up click areas for the UI. */
    public void setupClickAreas(){

        // Pause and quit click areas
        buttons.add(pauseGame);
        buttons.add(quitGame);

        // Gem click areas
        ClickArea cl;
        for(int i=0; i<g.GRID_WIDTH; i++){
            for(int j=0; j<g.GRID_WIDTH; j++){

                cl = new GemClickArea(g, i, j,
                        GEM_BOX_X + (r.GEM_SIZE * i),
                        GEM_BOX_Y + (r.GEM_SIZE * j),
                        r.GEM_SIZE,
                        r.GEM_SIZE
                        );

                buttons.add(cl);
            }
        }
    }

    /** Run the main game loop. */
    public void gameLoop(){

        // Attach mouse listeners
        r.addMouseListener(this);
        r.addMouseMotionListener(this);

        long ticksRemaining = GAME_LENGTH_IN_TICKS;
        while(ticksRemaining > 0 && !quitGame.isQuit()){

            drawInterface(ticksRemaining);

            // Check we're not near the entity limit.
            // In practice, we're not (hooray!) and use around 20k
            /* System.out.println("DEBUG: Entities: " + r.getEntityCount()); */
            /* System.out.println("DEBUG: fps: " + r.getFPS()); */

            r.delayAndClear();

            if(!pauseGame.isPaused())
                ticksRemaining -= 1;
        }

        // We don't care about the mouse any more
        r.removeMouseListener(this);
        r.removeMouseMotionListener(this);
    }

    // Draw all the boilerplate and UI stuff.
    private void drawInterface(long ticksRemaining){
        try{

            r.niceBackground();

            // Line down the LHS
            r.vline(180, 0, r.getHeight(), 2, "BLUE");

            // Text in the sidebar
            r.string("Gems!", 10, 10, r.fontBig, "WHITE", "");
            r.hline(10, 50, 160, 5, "BLUE");
            r.string("Score: ", 10, 60, r.fontSmall, "WHITE", "");
            r.string("" + g.getScore(), 10, 80, r.fontBig, "YELLOW", "");
            r.string("Time: ", 10, 130, r.fontSmall, "WHITE", "");
            r.string("" + ticksRemaining, 10, 160, r.fontBig, "YELLOW", "");
            r.hline(10, 200, (160.0/GAME_LENGTH_IN_TICKS) * ticksRemaining, 5, "YELLOW");
            r.string("combo: ", 10, 230, r.fontSmall, "white", "");
            r.string("" + g.lastCombo(), 10, 260, r.fontBig, "YELLOW", "");
            r.string("FPS:" + (int)Math.ceil(r.getFPS()), 515, 0, r.fontSmall, "GREY", "");

            if(pauseGame.isPaused())
                r.string("Paused", 40, 360, r.fontSmall, "RED", "");
            else
                r.string("[Pause]", 38, 360, r.fontSmall, "GREY", "");

            r.string("[Quit]", 43, 390, r.fontSmall, "GREY", "");

            // Box around gems
            r.box(GEM_BOX_X, GEM_BOX_Y, g.GRID_WIDTH * r.GEM_SIZE, g.GRID_WIDTH * r.GEM_SIZE, 1, "GREY");

            // Gems
            r.renderGems(g, GEM_BOX_X, GEM_BOX_Y);

        // Ignore these but warn on stderr
        }catch(Plotter.EntityLimitException e){
            System.err.println("Warning: entity limit reached!");
        }
    }





    /** Handle mouse clicks by looking for ClickAreas in the buttons ArrayList. */
    public void mouseClicked(MouseEvent e) {
        if(!pauseGame.isPaused()){
            for(ClickArea cl: buttons){
                cl.click(r.adjustXForInsets(e.getX()), r.adjustYForInsets(e.getY()));
            }
        }else{

            pauseGame.setPaused(false);
        }
    }



    /** Each time the mouse moves, handle hover states for the various buttons.
     * This implements the hover code by keeping track of each ClickArea.
     */
    public void mouseMoved(MouseEvent e){
        if(!pauseGame.isPaused()){

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

    /* Java interface cruft */
    private void checkButtonHoverStates() {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e){}

}
