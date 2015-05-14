package BRICK_GAME;
// run from GameSkeleton
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameHighscores extends BasicGameState {
// these are the parts required to use slick's "states" framework

    int stateID = -1;

    GameHighscores(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
//  creates the background manipulating
    Image background = null;
// creats fonts for printing text
    TrueTypeFont f;
// creates the back button
    MouseOverArea backArea;
    String[] names = new String[10];
    String[] scoresString = new String[10];
    int[] scores = new int[10];
    static boolean reset = false;
    boolean backOver = false;
    Sound cling1;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
// initializes various images
        background = new Image("MainMenuBackground.png");
        Image back = new Image("back.png");
        Image backPink = new Image("backPink.png");
// creates the back button
        backArea = new MouseOverArea(container, back, 325, 650, back.getWidth(), back.getHeight());
        backArea.setMouseOverImage(backPink);
// creates a sound when the back button is hovered over
        cling1 = new Sound("cling_1.wav");
// font initalizations
// lucida console is a monospaced font to aid in padding of spaces and commas
        f = new TrueTypeFont(new Font("Lucida Console", Font.BOLD, 30), true);
    }
// this reset deals with re-reading highscores.txt to update it incase the stats have changed
    void reset() throws FileNotFoundException {
// creates a scanner and scans in the highscores from highscores.txt
        Scanner file = new Scanner(new FileReader("Highscores.txt"));
        for (int load = 0; load < 10; load++) {
            String temp = file.nextLine();
            String[] temp2 = temp.split("//%%..");
            names[load] = temp2[0];
            scoresString[load] = temp2[1].trim();
        }
        file.close();
// loads a new array with the string versions of the scores
        for (int load = 0; load < scoresString.length; load++) {
            scores[load] = Integer.parseInt(scoresString[load]);
        }
// bubble sorts the scores in case the txt file is not in order
        for (int pass = 1; pass <= scores.length - 1; pass++) {
            for (int compare = 0; compare <= scores.length - 1 - pass; compare++) {
                if (scores[compare] < scores[compare + 1]) {
                    int temp = scores[compare];
                    scores[compare] = scores[compare + 1];
                    scores[compare + 1] = temp;
                    String temp2 = names[compare];
                    names[compare] = names[compare + 1];
                    names[compare + 1] = temp2;
                    temp2 = scoresString[compare];
                    scoresString[compare] = scoresString[compare + 1];
                    scoresString[compare + 1] = temp2;
                }
            }
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
// checks for reset commands
        if (reset) {
            reset = false;
// try-catch block is required to have FileNotFoundException for the reset
            try {
                reset();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GameHighscores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
// creates sound for the mouse-over of the back button
        if (backArea.isMouseOver()) {
            if (!backOver) {
                cling1.play();
                backOver = true;
            }
        } else {
            backOver = false;
        }
// enables to back button to change states
        Input input = container.getInput();
        if (backArea.isMouseOver() && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            game.enterState(GameSkeleton.GAMEMAINMENU);
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
// renders the background and back button first
        background.draw(0, 0);
        backArea.render(container, graphics);
// this draws the rank, name, and score from the highscores list
// also adds space padding and commas
        for (int rank = 1; rank < 11; rank++) {
            f.drawString(100, 100 + 50 * rank, rank + "");
            f.drawString(175, 100 + 50 * rank, names[rank - 1]);

            char[] temp = scoresString[rank - 1].toCharArray();
            String finalNum = "";
            int counter1 = -1;
            for (int counter2 = scoresString[rank - 1].length() - 1; counter2 >= 0; counter2--) {
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
            f.drawString(400, 100 + 50 * rank, print + finalNum);
        }
    }
}
