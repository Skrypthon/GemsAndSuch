
import java.util.ArrayList;

public class RenderGrid{

    // Store the renderable gems in a grid
    public RenderGem[] grid;
    private int gridSize;

    // Gems in the act of fading out, that are not on the grid but still need rendering
    public RenderGem[] spareGems = new RenderGem[100];
    
    
    // Screen position
    private double gemSize;
    private double screenX;
    private double screenY;

    public RenderGrid(GemGame g, double screenX, double screenY, double gemSize){

        this.screenX = screenX;
        this.screenY = screenY;
        this.gemSize = gemSize;
        
        initRenderGems(g);
    }

   /** Callback from GemUpdateListener. */
    public void swapGems(int x, int y, int x2, int y2){
        /* System.out.println("Swapping gems from " + x + "," + y + " to " + x2 + "," + y2); */

        RenderGem rg  = grid[getGemIndex(x, y)  ];
        RenderGem rg2 = grid[getGemIndex(x2, y2)];


        // Move gems, using animations
        rg.setAnimation(  new GemAnimation( 200, getScreenX(x2) - getScreenX(x), getScreenY(y2) - getScreenY(y), 0, 0 ) );
        rg2.setAnimation( new GemAnimation( 200, getScreenX(x) - getScreenX(x2), getScreenY(y) - getScreenY(y2), 0, 0) );

        // Then move in our model
        grid[ getGemIndex(x, y) ]   = rg2;
        grid[ getGemIndex(x2, y2) ] = rg;
    }


    /** Callback from GemUpdateListener. */
    public void moveGem(int x, int y, int x2, int y2){
        /* System.out.println("Moving gem from " + x + "," + y + " to " + x2 + "," + y2); */

        RenderGem rg = grid[getGemIndex(x, y)];

        // Move gem position on screen
        rg.setAnimation( new GemAnimation( 200, getScreenX(x2) - getScreenX(x), getScreenY(y2) - getScreenY(y), 0, 0 ) );

        // Then move in our model
        grid[ getGemIndex(x2, y2) ] = rg;
    }

    /** Callback from GemUpdateListener. */
    public void newGem(int x, int y, Gem gem){
        // TODO: animation
        /* System.out.println("New gem at " + x + "," + y); */

        // Create a gem with 0 visibility
        RenderGem newGem = new RenderGem(gem,  getScreenX(x), getScreenY(y), 1, 0);
        grid[ getGemIndex(x, y) ] = newGem;

        // Make it fade in
        newGem.setAnimation( new GemAnimation( 200, 0, 0, 0, 1 ));
    }

    /** Callback from GemUpdateListener. */
    public void delGem(int x, int y){
        // TODO: animation
        /* System.out.println("Removing gem from " + x + "," + y); */

        // Add scaling to gems, then add to the overflow set
        RenderGem rg = grid[getGemIndex(x, y)];
        rg.setAnimation( new GemAnimation( 100, 0, 0, 0, -rg.getScale() )); // Scale down
        rg.setAnimation( new GemAnimation( 100, 0, 0, -rg.getVisibility(), 0 )); // Disappear

        // Place in first slot we see in spareGems
        for(int i=0; i<spareGems.length; i++)
            if(spareGems[i] == null)
                spareGems[i] = rg;
       
        // Forget from the grid
        grid[ getGemIndex(x, y) ] = null;
    }


    /** Access the list of spare gems for rendering. */
    public RenderGem[] getSpareGems(){
        return spareGems;
    }

    /** Returns the internal list of RenderGems, used in rendering. */
    public RenderGem[] getGrid(){
        return grid;
    }

    /** Initialise the renderable wrappers around the gem array. */
    public void initRenderGems(GemGame g){
        // Init variables
        grid = new RenderGem[ g.GRID_WIDTH * g.GRID_WIDTH ];
        this.gridSize = g.GRID_WIDTH;

        // Copy gems into the renderable grid
        for(int i=0; i<gridSize; i++){
            for(int j=0; j<gridSize; j++){
                grid[ getGemIndex(i, j) ] = new RenderGem( g.getGemXY(i, j), getScreenX(i), getScreenY(j), 1, 1);
            }
        }
    }


    private double getScreenX(int x){
        return screenX + gemSize * x;
    }

    private double getScreenY(int y){
        return screenY + gemSize * y;
    }

    /** Returns the renderable gem at position x, y. */
    public RenderGem getGem(int x, int y){
        return grid[getGemIndex(x, y)];
    }

    
    /** Get index from x-y coords */
    private int getGemIndex(int row, int col){
        return row * gridSize + col;
    }


}
