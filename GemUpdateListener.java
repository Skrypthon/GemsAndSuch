

public interface GemUpdateListener{

    public void swapGems(int x, int y, int x2, int y2);

    /** Called when gems in grid positions x,y and x2,y2 are about to be swapped. */
    public void moveGem(int x, int y, int x2, int y2);


    public void newGem(int x, int y, Gem gem);


    public void delGem(int x, int y);
    
}
