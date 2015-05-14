package BRICK_GAME;
// run from GameSkeleton
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameSkeleton extends StateBasedGame {
// this makes the the state ID numbers
    public static final int GAMEMAINMENU = 0;
    public static final int GAMEOPTIONS = 1;
    public static final int GAMEENDGAME = 2;
    public static final int GAMEHIGHSCORES = 3;
    public static final int GAMEGAMEPLAY = 4;
    public static final int GAMEHIGHSCORESANIMATION = 5;
    public static final int GAMECREDITS = 6;

    public GameSkeleton() {
        super("BRICK");
// this creates the states from the various classes
        this.addState(new GameMainMenu(GAMEMAINMENU));
        this.addState(new GameOptions(GAMEOPTIONS));
        this.addState(new GameEndGame(GAMEENDGAME));
        this.addState(new GameHighscores(GAMEHIGHSCORES));
        this.addState(new GameGamePlay(GAMEGAMEPLAY));
        this.addState(new GameHighScoresAnimation(GAMEHIGHSCORESANIMATION));
        this.addState(new GameCredits(GAMECREDITS));
        this.enterState(GAMEMAINMENU);
    }
// this starts the game
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new GameSkeleton());
// tells the computer to lock the framerate at 60 FPS
// if you want to make the game easier, lower framerate
        app.setTargetFrameRate(60);
// sets the screen size as 800x800
        app.setDisplayMode(800, 800, false);
        app.start();
    }
// this adds the initalizes all the various states. In effect it loads all the system resouces
    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(GAMEOPTIONS).init(gameContainer, this);
        this.getState(GAMEENDGAME).init(gameContainer, this);
        this.getState(GAMEHIGHSCORES).init(gameContainer, this);
        this.getState(GAMEGAMEPLAY).init(gameContainer, this);
        this.getState(GAMEHIGHSCORESANIMATION).init(gameContainer, this);
        this.getState(GAMEMAINMENU).init(gameContainer, this);
    }
}
