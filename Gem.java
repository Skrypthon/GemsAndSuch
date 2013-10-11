

/** Represents a single gem to the game. */

public class Gem{

    public int type;
    private boolean active = false;
    private boolean hover = false;
    
    public Gem(int type){
        this.type = type;
    }

    // New gem with random type ( range 0-5 )
    public Gem(){
        this.type = (int)Math.floor(Math.random() * 6);
    }

    public void hoverIn(){
        hover = true;
    }

    public void hoverOut(){
        hover = false;
    }

    public void activate(){
        active = true;
    }

    public void deactivate(){
        active = false;
    }

    public boolean isActive(){
        return this.active;
    }

    public boolean isHover(){
        return this.hover;
    }
}

