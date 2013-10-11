

/** Manages the game's "continuation" for the Controller. */
public class QuitClickArea extends ClickArea{


    // Keep a handle to the controller
    private GemGameController g;

    // Should the game quit?
    private boolean quit = false;

    /** Maintains the game continuation state, i.e. if the user wishes to finish the game. */
    public QuitClickArea( GemGameController controller, int x, int y, int w, int h){
        super(x, y, w, h);

        this.g = g;
    }


    /** Updates internal state to quit the game. */
    public void click(){
        this.quit = true;
    }

    /** Returns true if the game should quit. */
    public boolean isQuit(){
        return quit;
    }

}
