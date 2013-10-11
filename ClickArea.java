

public class ClickArea{
    

    public int xmin;
    public int ymin;
    public int xmax;
    public int ymax;

    // Store if we had the mouse over us last time
    public boolean hoverstate;

    public ClickArea(int x, int y, int width, int height){
        xmin = x;
        ymin = y;
        xmax = x + width;
        ymax = y + height;
    }

    // Manage hovering by getting the current hover state of this area
    public boolean getHoverState(){
        return this.hoverstate;
    }

    // Set the current hover state
    public void setHoverState(boolean hs){
        this.hoverstate = hs;
    }


    public boolean isInBounds(int x, int y){
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax;
    }

    public void click(int x, int y){
        if(isInBounds(x, y))
            click();
    }

    public void hoverin(int x, int y){
        if(isInBounds(x, y))
            hoverin();
    }

    public void hoverout(int x, int y){
        if(isInBounds(x, y))
            hoverout();
    }

    public void hoverout(){
        // Override me
    }

    public void hoverin(){
        // Override me
    }

    // Trigger unconditionally
    public void click(){
        // Perform some action. STUB
    }



}
