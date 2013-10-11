
import java.awt.Point;
import java.util.ArrayList;

public class GemGame{

    private GemRenderer r;

    public static final int MIN_GROUP_SIZE = 4;
    public static final int GRID_WIDTH = 8;
    private Gem[] grid;

    // Selection variables
    private Gem halfSelection = null;
    private int halfSelectedX = -1;
    private int halfSelectedY = -1;

    // Score!
    private int score = 0;
    private int scorePerClick = 0; // Used to weight bonus score

    public GemGame() throws java.io.IOException{
        initGems();
    }

    public int getScore(){
        return score;
    }

    // Fill the grid with random gems
    private void initGems(){
        
        grid = new Gem[ GRID_WIDTH * GRID_WIDTH ];

        for(int i=0; i<grid.length; i++){
            grid[i] = new Gem();
        }

        // Delete from the bottom to force "settling"
        // TODO

        // Reset score afterwards
        score = 0;
    }

    public void selectGem(Gem gem){
        // Find gem
        int index = findGemIndex(gem);

    }

    public void selectGem(int index){
        int gemx = getGemX(index);
        int gemy = getGemY(index);
    }

    public void selectGem(int x, int y){

        System.out.println("Select gem " + x + ", " + y);

        Gem gem = getGemXY(x, y);
        scorePerClick = 0;  // Reset cascade score

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
            gem.activate();
            halfSelection = gem;
            halfSelectedX = x;
            halfSelectedY = y;
        }

    }


    /** Swap a gem with the one in the selection buffer. */
    private void swap(int x, int y, int x2, int y2){

        // Swap using the selection buffer
        int idx1 = getGemIndex(x, y);
        int idx2 = getGemIndex(x2, y2);

        Gem temp = grid[idx2];
        grid[idx2] = grid[idx1];
        grid[idx1] = temp;

        // Deselect everything involved
        grid[idx1].deactivate();
        grid[idx2].deactivate();
        grid[idx1].hoverOut();
        grid[idx2].hoverOut();
    }

    // Get the group size from the last interaction
    public int lastCombo(){
        return scorePerClick;
    }
        

    // Recursively spider and count the number of adjacent gems
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

    // Compare gems taking into account the limits of the plane
    private int getGemType(int x, int y){
        if( x < 0 || y < 0 || x >= GRID_WIDTH || y >= GRID_WIDTH )
            return -1;

        return getGemXY(x, y).type;
    }


    // Delete a gem and shift other down, filling in the grid at the top
    private void deleteAndShift(int x, int y){

        // Shift all the gems up, overwriting the dead one and losing its ref.
        for( int i=y; i>0; i--){
            grid[ getGemIndex(x, i) ] = grid[ getGemIndex(x, i-1) ];
        }

        // Add a new random gem at the top
        grid[ getGemIndex(x, 0) ] = new Gem();

        // Then consider the cascade of every gem "up" from the deleted one
        for(int i=y; i>0; i--){
            cascadeFrom(x, i);
        }
    }



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


    // Test if a co-ordinate is immediately adjacent to the
    // existing half-selection.
    public boolean adjacent(int x, int y){

        // If nothing is selected, say no.
        if(halfSelection == null)
            return false;

        // Test diff
        int xdiff = Math.abs(halfSelectedX - x);
        int ydiff = Math.abs(halfSelectedY - y);

        return (xdiff + ydiff <= 1);
    }

    public void hoverIn(int x, int y){

        if(halfSelection == null){
            getGemXY(x, y).hoverIn();
        }else{
            if(adjacent(x, y))
                getGemXY(x, y).hoverIn();
        }
    }

    public void hoverOut(int x, int y){
        // TODO: logic
        getGemXY(x, y).hoverOut();
    }

    public int getGemIndex(int row, int col){
        return row * GRID_WIDTH + col;
    }

    // Get gem from X-Y lookup.
    public Gem getGemXY(int row, int col){
        return grid[ getGemIndex(row, col) ];
    }

    private int getGemX(int index){
        return index % GRID_WIDTH;
    }

    private int getGemY(int index){
        return (int)Math.floor(index / GRID_WIDTH);
    }

    // Return index based on gem object
    private int findGemIndex(Gem gem){
        for(int i=0; i<grid.length; i++){
            if(gem == grid[i])
                return i;
        }
        return -1;
    }

}
