

public class GemClickArea extends ClickArea{


    private GemGame game;

    private int gemx;
    private int gemy;

    public GemClickArea( GemGame game, int gemx, int gemy, int x, int y, int w, int h){
        super(x, y, w, h);

        this.game = game;

        this.gemx = gemx;
        this.gemy = gemy;
    }


    public void hoverout(){
        game.hoverOut( gemx, gemy );
    }

    public void hoverin(){
        game.hoverIn( gemx, gemy );
    }

    public void click(){
        System.out.println("CLICK");

        game.selectGem(gemx, gemy);
    }

}
