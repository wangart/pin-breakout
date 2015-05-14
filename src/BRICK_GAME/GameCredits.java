package BRICK_GAME;
// run from GameSkeleton
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameCredits extends BasicGameState {
// these are the parts required to use slick's "states" framework

    int stateID = -1;

    GameCredits(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
// various image initializations
    Image back = null;
    Image backPink = null;
    Image background = null;
    Image credits = null;
    Image onScreenCredits = null;
    MouseOverArea backArea;
    TrueTypeFont f;
    static boolean reset = false; // this boolean is to trigger a reset of the class
    int interval = 400;
    int starty = 200;
    int cut = 0;
    long framesPassed = 0;

// init is the initializations that are never to be changed, and don't need to be reset
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        credits = new Image("credits.png");
        background = new Image("mainMenuBackgroundRed.png");
        back = new Image("back.png");
        backPink = new Image("backPink.png");
        onScreenCredits = new Image("credits.png");
        backArea = new MouseOverArea(container, back, 325, 650, back.getWidth(), back.getHeight());
        backArea.setMouseOverImage(backPink);
    }
// this sets the class to essentially the same state it began with

    void reset() {
        cut = 0;
        framesPassed = 0;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
// check if a reset should be initiated
        if (reset) {
            reset = false;
            reset();
        }
// this changed the shown image every 3 frames
        framesPassed++;
        if (framesPassed % 5 == 0) {
            cut++;
        }
        onScreenCredits = credits.getSubImage(0, cut, 800, interval);
// this takes care of control for the back button
        if (backArea.isMouseOver() && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            game.enterState(GameSkeleton.GAMEMAINMENU);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
// draws the background, along with the back button and a slice of the credits picture
        background.draw(0, 0);
        backArea.render(container, g);
        onScreenCredits.draw(0, starty);
    }
}