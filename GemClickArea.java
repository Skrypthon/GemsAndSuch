
/** Responds to clicks in the gem area by notifying the game model. */
public class GemClickArea extends ClickArea{

    // Store a handle to the game model
    private GemGame game;

    // Store the game grid position
    private int gemx;
    private int gemy;

    /** Allows a user to click on a gem.  Passes most of its logic through to the Game model.
     *
     * @param game The game model to pass events to.
     * @param gemx The gem X co-ordinate in the grid
     * @param gemy The gem y-co-ord in the grid.
     */
    public GemClickArea( GemGame game, int gemx, int gemy, int x, int y, int w, int h){
        super(x, y, w, h);

        this.game = game;

        this.gemx = gemx;
        this.gemy = gemy;
    }


    /** Alerts the game that a gem is no longer a candidate for selection. */
    public void hoverout(){
        game.hoverOut( gemx, gemy );
    }

    /** Alerts the game that a gem is a candidate for selection, allowing it
     * to apply game rules to the highlighting process. */
    public void hoverin(){
        game.hoverIn( gemx, gemy );
    }

    /** Tells the game the user wishes to select a given gem. */
    public void click(){
        game.selectGem(gemx, gemy);
    }

}
