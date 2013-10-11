

/** Handles pausing. */
public class PauseClickArea extends ClickArea{

    // Keep handles to the controller
    private GemGameController g;

    // Keep paused state
    private boolean paused = false;

    /** Maintains the paused state of the UI. */
    public PauseClickArea( GemGameController controller, int x, int y, int w, int h){
        super(x, y, w, h);

        this.g = g;
    }


    /** Called when the pause button is clicked.  Updates internal state. */
    public void click(){
        setPaused(true);
    }

    /** Checks if the game is (should be) paused. */
    public boolean isPaused(){
        return this.paused;
    }

    /** Sets the paused state.  Used to cancel paused-ness when any click happens. */
    public void setPaused(boolean paused){
        this.paused = paused;
    }
}
