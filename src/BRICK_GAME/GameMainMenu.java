package BRICK_GAME;
// run from GameSkeleton
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameMainMenu extends BasicGameState {
// these are the parts required to use slick's "states" framework
    int stateID = -1;

    GameMainMenu(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
// initalizes various images for the menu
    Image background = null;
    Image[] normal = new Image[5];
    Image[] inverted = new Image[5];
    Image[] drawnImage = new Image[5];
// placement of various buttons
    int startx = 50;
    int starty = 300;
    int optionsx = 430;
    int optionsy = 200;
    int creditsx = 510;
    int creditsy = 300;
    int highscoresx = 510;
    int highscoresy = 400;
    int exitx = 430;
    int exity = 500;
    int xBall = 400;
    int yBall = 400;
// loads menu music and sound
    Music track1;
    Sound cling1;
// this makes the sound play once for each button over instead of over and over
    boolean startOver = false;
    boolean optionsOver = false;
    boolean creditsOver = false;
    boolean highScoresOver = false;
    boolean exitOver = false;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
// loads various images from the hard disk
        background = new Image("MainMenuBackground.png");
        Image menu = new Image("MenuOptions.png");
        Image invertedMenu = new Image("MenuOptionsPink.png");
// this cuts the menu and inverted menu into the 5 respective buttons
        int y = 0;
        for (int load = 1; load < 5; load++) {
            normal[load] = menu.getSubImage(0, y, 299, 60);
            inverted[load] = invertedMenu.getSubImage(0, y, 299, 60);
// the drawn images must be initialized, and so a random image is loaded
            drawnImage[load] = new Image("ddd.png");
            y += 60;
        }
// this cuts out the start button, which uses different spacing
        drawnImage[0] = new Image("ddd.png");
        normal[0] = menu.getSubImage(0, y, 299, 150);
        inverted[0] = invertedMenu.getSubImage(0, y, 299, 150);
// load music from the hard drive
        track1 = new Music("mainMenuMusic.wav");
        cling1 = new Sound("cling_1.wav");
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
// this makes it so that if the track isn't playing, it forces the track to play
        if (!track1.playing()) {
            track1.loop();
        }
// various controls to change the colour of the lables with mouse overed
        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        boolean startSelected = false;
        boolean optionsSelected = false;
        boolean creditsSelected = false;
        boolean highScoresSelected = false;
        boolean exitSelected = false;

        if ((mouseX >= startx && mouseX <= startx + normal[0].getWidth()) && (mouseY >= starty && mouseY <= starty + normal[0].getHeight())) {
            startSelected = true;
        } else if ((mouseX >= optionsx && mouseX <= optionsx + normal[1].getWidth() * 2 / 3) && (mouseY >= optionsy && mouseY <= optionsy + normal[1].getHeight())) {
            optionsSelected = true;
        } else if ((mouseX >= creditsx && mouseX <= creditsx + normal[2].getWidth() / 1.5) && (mouseY >= creditsy && mouseY <= creditsy + normal[2].getHeight())) {
            creditsSelected = true;
        } else if ((mouseX >= highscoresx && mouseX <= highscoresx + normal[3].getWidth()) && (mouseY >= highscoresy && mouseY <= highscoresy + normal[3].getHeight())) {
            highScoresSelected = true;
        } else if ((mouseX >= exitx && mouseX <= exitx + normal[4].getWidth() / 3 * 1.2) && (mouseY >= exity && mouseY <= exity + normal[4].getHeight())) {
            exitSelected = true;
        }
// IF START IS SELECTED
        if (startSelected) {
            if (!startOver) {
                cling1.play();
                startOver = true;
            }
            drawnImage[0] = inverted[0];
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                GameGamePlay.reset = true;
                game.enterState(GameSkeleton.GAMEGAMEPLAY);
            }
        } else {
            drawnImage[0] = normal[0];
            startOver = false;
        }
// IF OPTIONS IS SELECTED
        if (optionsSelected) {
            if (!optionsOver) {
                cling1.play();
                optionsOver = true;
            }
            drawnImage[1] = inverted[1];
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                game.enterState(GameSkeleton.GAMEOPTIONS);
            }
        } else {
            drawnImage[1] = normal[1];
            optionsOver = false;
        }
// IF CREDITS IS SELECTED
        if (creditsSelected) {
            if (!creditsOver) {
                cling1.play();
                creditsOver = true;
            }
            drawnImage[2] = inverted[2];
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                GameCredits.reset = true;
                game.enterState(GameSkeleton.GAMECREDITS);
            }
        } else {
            drawnImage[2] = normal[2];
            creditsOver = false;
        }
// IF HIGHSCORES IS SELECTED
        if (highScoresSelected) {
            if (!highScoresOver) {
                cling1.play();
                highScoresOver = true;
            }
            drawnImage[3] = inverted[3];
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                GameHighscores.reset = true;
                game.enterState(GameSkeleton.GAMEHIGHSCORES);
            }
        } else {
            drawnImage[3] = normal[3];
            highScoresOver = false;
        }
// IF EXIT IS SELECTED
        if (exitSelected) {
            if (!exitOver) {
                cling1.play();
                exitOver = true;
            }
            drawnImage[4] = inverted[4];
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                container.exit();
            }
        } else {
            drawnImage[4] = normal[4];
            exitOver = false;
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
// draws background and all the options
        background.draw(0, 0);
        drawnImage[0].draw(startx, starty);
        drawnImage[1].draw(optionsx, optionsy);
        drawnImage[2].draw(creditsx, creditsy);
        drawnImage[3].draw(highscoresx, highscoresy);
        drawnImage[4].draw(exitx, exity);
    }
}