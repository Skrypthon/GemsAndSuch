

import java.util.LinkedList;

/** Manages the rendering of a gem, handling animations to move between states. */
public class RenderGem{

    public Gem gem;

    // Screen positions
    private double rx;
    private double ry;
    
    // Modifies screen position
    private GemAnimation animation;
    private LinkedList<GemAnimation> animationQueue = new LinkedList<GemAnimation>();

    // Stores hover state
    private boolean hover = false;

    // Opacity
    private double visibility = 1.0;
    private double scale = 1.0;

    public RenderGem(Gem gem, double rx, double ry, double visibility, double scale){
        this.gem = gem;

        this.rx = rx;
        this.ry = ry;

        // 0-1 opacity
        this.visibility = visibility;

        // Size modifier
        this.scale = scale;
    }

    /** Set an animation going on the gem. */
    public void setAnimation(GemAnimation ga){
        animationQueue.add(ga);
    }

    /** Clear any animation on this object. */
    public void nextAnimation(){

        // Clear any existing one and update location
        if(animation != null){
            this.rx += animation.getFinalDX();
            this.ry += animation.getFinalDY();
            this.visibility += animation.getFinalDVis();
            this.scale += animation.getFinalDScale();
        }

        // Move onto the next animation
        if(animationQueue.size() > 0){
            this.animation = animationQueue.removeFirst();
            this.animation.start();
        }else{
            this.animation = null;
        }
    }

    /** Return the current active animation. */
    public GemAnimation getAnimation(){
        return this.animation;
    }

    /** Set screen location. */
    public void setScreenX(double rx){
        this.rx = rx;
    }
   
    /** Set screen location. */
    public void setScreenY(double ry){
        this.ry = ry;
    }

    /** Returns the on-screen X position. */
    public double getScreenX(){
        // Load animation if none exists
        if(animation == null && animationQueue.size() > 0)
            nextAnimation();

        // Check animation
        if(animation != null){
            if(animation.isEnded())
                nextAnimation();
            else
                return rx + animation.getDX();
        }
        return rx;
    }

    /** Returns the on-screen Y position. */
    public double getScreenY(){
        // Load animation if none exists
        if(animation == null && animationQueue.size() > 0)
            nextAnimation();
        
        // Check animation
        if(animation != null){
            if(animation.isEnded()){
                nextAnimation();
            }else
                return ry + animation.getDY();
        }
        return ry;
    }

    public double getVisibility(){
        // Load animation if none exists
        if(animation == null && animationQueue.size() > 0)
            nextAnimation();

        // Check animation
        if(animation != null){
            if(animation.isEnded()){
                nextAnimation();
            }else
                return visibility + animation.getDVis();
        }
        return visibility;
    }

    public double getScale(){
        // Load animation if none exists
        if(animation == null && animationQueue.size() > 0)
            nextAnimation();

        // Check animation
        if(animation != null){
            if(animation.isEnded()){
                nextAnimation();
            }else
                return scale + animation.getDScale();
        }
        return scale;
    }

    /** Sets the hover state to the given value. */
    public void setHover(boolean h){
        this.hover = h;
    }

    /** Is the hover state set? */
    public boolean isHover(){
        return this.hover;
    }

}
