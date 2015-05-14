package BRICK_GAME;
// run from GameSkeleton
import java.awt.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameEndGame extends BasicGameState {
// these are the parts required to use slick's "states" framework

    int stateID = -1;

    GameEndGame(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
// various image initializations
    Image background = null;
    Image back = null;
    Image backPink = null;
    Image drawnBack = null;
    Image submit = null;
    Image submitPink = null;
    Image drawnSubmit = null;
// these integers determine the new size of the back and submit images
    int backScaledHeight;
    int backScaledWidth;
    int submitScaledHeight;
    int submitScaledWidth;
// these determine the placement of the back and submit images
    int backx = 125;
    int backy = 500;
    int submitx = 565;
    int submity = 500;
// classes and variables required for text manipulation
    TrueTypeFont f;
    TextField field;
    int currentHighscore;
    int drawnNumber = 0;
    String draw;
    static String name; // a static string so that the highscores animation class can retrieve it
    static boolean reset = false; // this boolean is to trigger a reset of the class

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
// this loads various images
        background = new Image("MainMenuBackground.png");
        back = new Image("back.png");
        backPink = new Image("backPink.png");
        drawnBack = new Image("backPink.png");
        submit = new Image("submit.png");
        submitPink = new Image("submitPink.png");
        drawnSubmit = new Image("submitPink.png");

// scales the images based on their size
        backScaledHeight = (int) Math.round(back.getHeight() / 1.5);
        backScaledWidth = (int) Math.round(back.getWidth() / 1.5);
        submitScaledHeight = (int) Math.round(submit.getHeight() / 1.5);
        submitScaledWidth = (int) Math.round(submit.getWidth() / 1.5);
// makes fonts and the text field for name entry
        f = new TrueTypeFont(new Font("Lucida Console", Font.BOLD, 30), true);
        field = new TextField(container, new TrueTypeFont(new Font("Lucida Console", Font.BOLD, 40), true), 250, 500, 300, 40);
        field.setFocus(true);
    }
// this method calls a reset of the class

    void reset() {
        currentHighscore = GameGamePlay.score;
        drawnNumber = 0;
        field.setText("");
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
// checks if a reset is initiated
        if (reset) {
            reset = false;
            reset();
        }
// this constantly sets keyboard focus on the field
        field.setFocus(true);
// booleans to control the selection of various buttons
        boolean backSelected = false;
        boolean submitSelected = false;
// checks for the back button of the things
        if ((mouseX >= backx && mouseX <= backx + backScaledWidth) && (mouseY >= backy && mouseY <= backy + backScaledHeight)) {
            backSelected = true;
        } else if ((mouseX >= submitx && mouseX <= submitx + submitScaledWidth) && (mouseY >= submity && mouseY <= submity + submitScaledHeight)) {
            submitSelected = true;
        }
// this changes the colour and checks for clicking of the buttons
        if (backSelected) {
            drawnBack = backPink;
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                game.enterState(GameSkeleton.GAMEMAINMENU);
            }
        } else {
            drawnBack = back;
        }
        if (submitSelected) {
            drawnSubmit = submitPink;
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)&& !field.getText().equalsIgnoreCase("")) {
                name = field.getText();
                GameHighScoresAnimation.reset = true;
                game.enterState(GameSkeleton.GAMEHIGHSCORESANIMATION);
            }
        } else {
            drawnSubmit = submit;
        }

// this ends the animation on a right click of the mouse
        if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
            drawnNumber = currentHighscore;
        }
// this creates the number counting up animation
        if (drawnNumber < currentHighscore) {
            drawnNumber += 5;
        }
// this pads zeros and adds commas to the drawn score
        char[] temp = (Integer.toString(drawnNumber)).toCharArray();
        String finalNum = "";
        int counter1 = -1;
        for (int counter2 = temp.length - 1; counter2 >= 0; counter2--) {
            counter1++;
            if (counter1 == 3) {
                finalNum = "," + finalNum;
                counter1 = 0;
            }
            finalNum = temp[counter2] + finalNum;
        }
        String print = "";
        for (int pad = 1; pad <= (17 - finalNum.length()); pad++) {
            print += " ";
        }
        draw = print + finalNum;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
// renders all the elements of the class
        background.draw(0, 0);
        drawnBack.draw(backx, backy, backScaledWidth, backScaledHeight);
        drawnSubmit.draw(submitx, submity, submitScaledWidth, submitScaledHeight);
        f.drawString(185, 450, draw);
        field.render(container, graphics);
    }
}
