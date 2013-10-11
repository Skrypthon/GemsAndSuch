


public class QuitClickArea extends ClickArea{


    private GemGameController g;
    private boolean quit = false;

    public QuitClickArea( GemGameController controller, int x, int y, int w, int h){
        super(x, y, w, h);

        this.g = g;
    }


    public void click(){
        this.quit = true;
    }

    public boolean isQuit(){
        return quit;
    }

}
