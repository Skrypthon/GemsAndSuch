


public class PauseClickArea extends ClickArea{


    private GemGameController g;
    private boolean paused = false;

    public PauseClickArea( GemGameController controller, int x, int y, int w, int h){
        super(x, y, w, h);

        this.g = g;
    }


    public void click(){
    
        System.out.println("PAUSING");

        setPaused(true);
    }

    public boolean isPaused(){
        return this.paused;
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }
}
