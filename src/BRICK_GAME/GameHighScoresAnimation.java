package BRICK_GAME;
// run from GameSkeleton
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameHighScoresAnimation extends BasicGameState {
// these are the parts required to use slick's "states" framework

    int stateID = -1;

    GameHighScoresAnimation(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
// various image initializations
    Image background = null;
    Image back = null;
    Image spectra = null;
// various arrays to do the writing and sorting of the highscores
    String[] names = new String[10];
    String[] scoresString = new String[10];
    int[] scores = new int[10];
    String[] prePrintNames = new String[11];
    int[] prePrintScores = new int[11];
// various stats from the last game
    static String currentName;
    int currentHighscore;
    int currentRank;
    TrueTypeFont f;
    MouseOverArea backArea;
    int backx = 125;
    int backy = 500;
    int currentSpace;
    int framesPassed;
    static boolean reset = false; // this boolean checks for reset commands by other classes

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
// init is the initializations that are never to be changed, and don't need to be reset
// image assingments
        background = new Image("MainMenuBackground.png");
        spectra = new Image("rectSpectra.png");
// font initalizations
// lucida console is a monospaced font to aid in padding of spaces and commas
        f = new TrueTypeFont(new Font("Lucida Console", Font.BOLD, 30), true);
// creating the back button
        back = new Image("back.png");
        backArea = new MouseOverArea(container, back, 300, 650, back.getWidth(), back.getHeight());
        backArea.setMouseOverImage(new Image("backPink.png"));
    }

// lengthy reset method that re-reads the highscores, updates it, and writes it back
// also creates an array for drawing the animation
// the reasion that these aren't combined is so that the highscores are still written even
// if the user chooses to leave before the animation ends.
    void reset() {
// resets variables to manage the animation
        currentRank = 0;
        currentSpace = 0;
        framesPassed = 0;
// scans the highscores.txt file into the program
// includes a "try-catch" block to catch the FileNotFoundException
        try {
            Scanner file = new Scanner(new FileReader("Highscores.txt"));
            for (int load = 0; load < 10; load++) {
                String temp = file.nextLine();
                String[] temp2 = temp.split("//%%..");
//  "//%%.." is to aviod splitting by random characters from the name
                names[load] = temp2[0];
                scoresString[load] = temp2[1].trim();
            }
            file.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameHighScoresAnimation.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int load = 0; load < scoresString.length; load++) {
            scores[load] = Integer.parseInt(scoresString[load]);
        }
// bubble sorts the numbers read form the text file
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
// this copies the array into another array with 11 slots
        for (int copy = 0; copy < 10; copy++) {
            prePrintNames[copy] = names[copy];
            prePrintScores[copy] = scores[copy];
        }
// grabs the player's new name and score form the previous states
        currentName = GameEndGame.name;
        currentHighscore = GameGamePlay.score;
// adds the current highscore and name into the new array
        prePrintNames[10] = currentName;
        prePrintScores[10] = currentHighscore;
// bubble sorts the array that has the current highscore plus the old ones form the text file
        for (int pass = 1; pass <= prePrintScores.length - 1; pass++) {
            for (int compare = 0; compare <= prePrintScores.length - 1 - pass; compare++) {
                if (prePrintScores[compare] < prePrintScores[compare + 1]) {
                    int temp = prePrintScores[compare];
                    prePrintScores[compare] = prePrintScores[compare + 1];
                    prePrintScores[compare + 1] = temp;
                    String temp2 = prePrintNames[compare];
                    prePrintNames[compare] = prePrintNames[compare + 1];
                    prePrintNames[compare + 1] = temp2;
                }
            }
        }
// prints the top 10 scores into the text file
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter("Highscores.txt"));
            for (int output = 0; output < 10; output++) {
                fileOut.println(prePrintNames[output] + "//%%.." + prePrintScores[output]);
            }
            fileOut.close();
        } catch (IOException ex) {
            Logger.getLogger(GameHighScoresAnimation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// everything after this is for the animation and has no bearing on the saved scores

    @Override
    public void update(GameContainer container, StateBasedGame game,
            int delta) throws SlickException {
        Input input = container.getInput();
// checks for reset commands
        if (reset) {
            reset = false;
            reset();
        }
// this activates back button
        if (backArea.isMouseOver() && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            game.enterState(GameSkeleton.GAMEMAINMENU);
        }
// this counts frames. Every 30 frames advances the animation
        framesPassed++;
        if (framesPassed % 30 == 0 && currentRank < 9) {
            currentRank += 1;
            currentSpace += 50;
        }
// this replaces the current ranked score on the list if it's smaller than the slected one
        if (currentHighscore > scores[currentRank]) {
            scoresString[currentRank] = Integer.toString(currentHighscore);
            int temp = scores[currentRank];
            scores[currentRank] = currentHighscore;
            currentHighscore = temp;
            String tempString = names[currentRank];
            names[currentRank] = currentName;
            currentName = tempString;
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game,
            Graphics graphics) throws SlickException {
// renders the background and the back button
        background.draw(0, 0);
        backArea.render(container, graphics);

// draws the current highscore with padded spacing and commas
// also draws the spectra outline around the score
        if (currentRank < 9) {
            char[] temp = (Integer.toString(currentHighscore)).toCharArray();
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
            for (int pad = 1; pad <= (13 - finalNum.length()); pad++) {
                print += " ";
            }
            f.drawString(520, 150 + currentSpace, print + finalNum);
            spectra.draw(0, 140 + currentSpace, 800, 50);
        }
// this draws the current rank, name, and score from the highscores list
// also adds space padding and commas
        for (int rank = 1; rank < 11; rank++) {
            f.drawString(25, 100 + 50 * rank, rank + "");
            f.drawString(75, 100 + 50 * rank, names[rank - 1]);

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
            f.drawString(150, 100 + 50 * rank, print + finalNum);
        }
    }
}
