
import java.awt.Point;
import java.util.ArrayList;

/** Primary game logic model.  Handles gem selection, deletion, positioning and scoring.
 */
public class GemGame{

    // Minimum group size necessary to score
    public static final int MIN_GROUP_SIZE = 4;

    // Grid size and grid storage as a linearised 2D array
    public static final int GRID_WIDTH = 8;
    private Gem[] grid;

    // Selection variables used when one gem is selected
    // but before a second one is.
    private Gem halfSelection = null;
    private int halfSelectedX = -1;
    private int halfSelectedY = -1;

    // Score!
    private int score = 0;
    private int scorePerClick = 0; // Used to weight bonus score

    // Callbacks (optional)
    private GemUpdateListener controller = null;

    /** Creates a new game with 0 score and random gems. */
    public GemGame() throws java.io.IOException{
        initGems();
    }

    public void addController(GemUpdateListener controller){
        this.controller = controller;
    }

    public void removeController(){
        this.controller = null;
    }

    /** Returns the total score to this point. */
    public int getScore(){
        return score;
    }

    /** Fill the grid with random gems. */
    private void initGems(){
        
        // New memory space
        grid = new Gem[ GRID_WIDTH * GRID_WIDTH ];

        // Fill with random gems
        for(int i=0; i<grid.length; i++){
            newGem(getGemX(i), getGemY(i));
        }

        // Delete from the bottom to force "settling"
        for(int i=0; i<GRID_WIDTH; i++){
            deleteAndShift(i, GRID_WIDTH - 1);
        }

        // Reset score afterwards
        score = 0;
        scorePerClick = 0;
    }

    /** Select a gem based on an object reference. */
    public void selectGem(Gem gem){
        // Find gem
        int index = findGemIndex(gem);
    }

    /** Select a gem using an index number. */
    public void selectGem(int index){
        int gemx = getGemX(index);
        int gemy = getGemY(index);
    }

    /** Select a gem using x-y coordinates. */
    public void selectGem(int x, int y){

        // Load the gem
        Gem gem = getGemXY(x, y);
        scorePerClick = 0;  // Reset cascade score

        // See if someone's already selected one
        if(halfSelection != null){
            if(gem == halfSelection){
                gem.deactivate();
                halfSelection = null;
            }else{
                if(adjacent(x, y)){
                    
                    /* System.out.println("Selecting " + x + ", " + y + " comp. to " + halfSelectedX + ", " + halfSelectedY); */

                    gem.activate();

                    // If the swap ended in groups, let it stand
                    swap(x, y, halfSelectedX, halfSelectedY);
                    halfSelection = null;

                    // NOTE: the single pipe is eager, so doesn't lazily skip the second check
                    // if the first one works.
                    if(!(cascadeFrom(halfSelectedX, halfSelectedY) | cascadeFrom(x, y))){
                        // If we didn't do anything, swap back!
                        swap(x, y, halfSelectedX, halfSelectedY);
                    }   


                }
            }
        }else{
            // If not, select one
            gem.activate();
            halfSelection = gem;
            halfSelectedX = x;
            halfSelectedY = y;
        }

    }


    /** Swap a gem with the one in the selection buffer. */
    private void swap(int x, int y, int x2, int y2){

        // Call the gem update listener if one exists
        if(controller != null){
            controller.swapGems(x, y, x2, y2);
            /* controller.moveGem(x,y, x2, y2); */
            /* controller.moveGem(x2,y2, x, y); */
        }

        // Swap using the selection buffer
        int idx1 = getGemIndex(x, y);
        int idx2 = getGemIndex(x2, y2);

        Gem temp = grid[idx2];
        grid[idx2] = grid[idx1];
        grid[idx1] = temp;

        // Deselect everything involved
        grid[idx1].deactivate();
        grid[idx2].deactivate();
    }

    /** Returns the score from the last user interaction, including all cascade bonuses. */
    public int lastCombo(){
        return scorePerClick;
    }
        

    /** Recursively spider and count the number of adjacent gems.
     * This only works for gems in a line.  If there are two dimensions of lines, the longer one
     * is preferred.
     *
     * @param x The x co-ordinate to start from
     * @param y The y co-ordinate to start from
     * @param group An array list that will be filled with points in the group when complete
     * @param horizontal Should this search horizontally or vertically?
     */
    private int countGroup(int x, int y, ArrayList<Point> group, boolean horizontal){
       
        // Return if out of bounds.
        if( x < 0 || y < 0 || x >= GRID_WIDTH || y >= GRID_WIDTH )
            return 0;

        // Return if seen before then add self to group
        for(Point p: group){
            if(p.x == x && p.y == y)
                return 0;
        }
        group.add(new Point(x, y));

       
        // Start a count and read current gem type
        int adjacent = 0;
        int type = getGemXY(x, y).type;


        // Recursively check all the others.  
        // Count one per matching gem.
        if(horizontal){
            if(getGemType(x+1, y) == type)
                adjacent += 1 + countGroup(x+1, y, group, horizontal);
            if(getGemType(x-1, y) == type)
                adjacent += 1 + countGroup(x-1, y, group, horizontal);
        }else{
            if(getGemType(x, y+1) == type)
                adjacent += 1 + countGroup(x, y+1, group, horizontal);
            if(getGemType(x, y-1) == type)
                adjacent += 1 + countGroup(x, y-1, group, horizontal);
        }

        return adjacent;
    }

    /** Compare gems taking into account the limits of the plane. 
     *
     * @return The gem type, or -1 if the point is beyond bounds.
     * */
    private int getGemType(int x, int y){
        if( x < 0 || y < 0 || x >= GRID_WIDTH || y >= GRID_WIDTH )
            return -1;

        return getGemXY(x, y).type;
    }


    /** Delete a gem and shift other down, filling in the grid at the top.
     *
     * Also calls cascadeFrom all the way up to ensure proper matching occurs.
     */
    private void deleteAndShift(int x, int y){

        if(controller != null)
            controller.delGem(x, y);

        // Shift all the gems up, overwriting the dead one and losing its ref.
        for( int i=y; i>0; i--){
            moveGem(x, i-1, x, i);
        }

        // Add a new random gem at the top
        newGem(x, 0);

        // Then consider the cascade of every gem "up" from the deleted one
        for(int i=y; i>=0; i--){
            cascadeFrom(x, i);
        }
    }

    /** Move a gem from x,y to x2,y2 */
    private void moveGem(int x, int y, int x2, int y2){

        grid[ getGemIndex(x2, y2) ] = grid[ getGemIndex(x, y) ];

        if(controller != null)
            controller.moveGem(x, y, x2, y2);
    }



    /** Create new gem at x,y with random type. */
    private void newGem(int x, int y){
        Gem gem = new Gem();
        grid[ getGemIndex(x, y) ] = gem;


        // Alert any listening controllers
        if(controller != null)
            controller.newGem(x, y, gem);
    }


    /** Group match, delete, and repeat until no more groups match in dirty areas.
     *
     * This is the core routine that ensures everything filters down.  It's recursive by invoking 
     * deleteAndShift.
     */
    private boolean cascadeFrom(int x, int y){
        // First, find the group that match
        ArrayList<Point> hgroup = new ArrayList<Point>();
        ArrayList<Point> vgroup = new ArrayList<Point>();
        int hcount = countGroup(x, y, hgroup, true);
        int vcount = countGroup(x, y, vgroup, false);

        // Track to see if we did or not
        boolean cascaded = false;
    
        if(Math.max(hcount, vcount) >= MIN_GROUP_SIZE){

            // Say we did
            cascaded = true;

            // Compute score based on group we're using,
            // should go up massively for cascades
            score += Math.max(hcount, vcount) + scorePerClick;
            scorePerClick += Math.max(hcount, vcount);

            // Delete groups, favouring the larger group direction.
            if(vcount > hcount){
                for(Point p: vgroup){
                    deleteAndShift(p.x, p.y);
                }
            }else{
                for(Point p: hgroup){
                    deleteAndShift(p.x, p.y);
                }
            }
        }

        return cascaded;
    }

    /** Test if a co-ordinate is immediately adjacent to the
     * existing half-selection.
     *
     * Requires that a half-selection exists.  If it does not
     * this will always return false.
     */
    public boolean adjacent(int x, int y){

        // If nothing is selected, say no.
        if(halfSelection == null)
            return false;

        // Test diff
        int xdiff = Math.abs(halfSelectedX - x);
        int ydiff = Math.abs(halfSelectedY - y);

        return (xdiff + ydiff <= 1);
    }

    /** Called when hovering over a gem.
     * Will highlight a gem only if it is a legal move.
     */
    public boolean isLegalMove(int x, int y){
        if(halfSelection == null){
            return true;
        }else{
            if(adjacent(x, y))
                return true;
        }
        return false;
    }

    /** Return the 1D index from 2D coordinates. */
    public int getGemIndex(int row, int col){
        return row * GRID_WIDTH + col;
    }

    /** Get gem from X-Y lookup. */
    public Gem getGemXY(int row, int col){
        return grid[ getGemIndex(row, col) ];
    }

    /** Return the X coordinate of a gem from its 1D index. */
    private int getGemX(int index){
        return index % GRID_WIDTH;
    }

    /** Return the Y coordinate of a gem from its 1D index. */
    private int getGemY(int index){
        return (int)Math.floor(index / GRID_WIDTH);
    }

    /** Return index based on gem object.  
     *
     * Slow and thus possibly never used...
     * */
    private int findGemIndex(Gem gem){
        for(int i=0; i<grid.length; i++){
            if(gem == grid[i])
                return i;
        }
        return -1;
    }

}
