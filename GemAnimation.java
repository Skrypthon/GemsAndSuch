

public class GemAnimation{


    // In ms
    private long startTime;
    private long duration;

    // How far to move
    private double dx;
    private double dy;
    private double dvis;
    private double dscale;

    public GemAnimation(long duration, double dx, double dy, double dvis, double dscale){

        this.startTime = System.currentTimeMillis();
        this.duration = duration;
        this.dx = dx;
        this.dy = dy;
        this.dvis = dvis;
        this.dscale = dscale;

        /* System.out.println("New animation taking " + duration + "ms moving " + dx + "," + dy + " ... " + this); */
    }

    public double getFinalDX(){
        return dx;
    }

    public double getFinalDY(){
        return dy;
    }

    public double getFinalDVis(){
        return dvis;
    }

    public double getFinalDScale(){
        return dscale;
    }

    /** (re)start the animation. */
    public void start(){
        this.startTime = System.currentTimeMillis();
    }

    /** Get the Y deviation position at the current time. */
    public double getDY(){
        return tween(dy);
    }

    /** Get the X deviation position at the current time. */
    public double getDX(){
        return tween(dx);
    }

    public double getDVis(){
        return tween(dvis);
    }

    public double getDScale(){
        return tween(dscale);
    }


    /** Has the animation ended? */
    public boolean isEnded(){
        return System.currentTimeMillis() > (startTime + duration);
    }


    private double tween(double limit){
        long now = System.currentTimeMillis();
        
        // Past end
        if(isEnded())
            return limit;

        // Compute proportion
        // TODO: Check precision isn't lost here
        double proportion = (double)(now - startTime) / (double)duration;

        return limit * proportion;
    }

}
