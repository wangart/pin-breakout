package BRICK_GAME;
// run from GameSkeleton
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOptions extends BasicGameState {
// these are the parts required to use slick's "states" framework
    int stateID = -1;

    GameOptions(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
// various image initializations
    Image background = null;
    Image music = null;
    Image musicPink = null;
    Image drawnMusic = null;
    Image sound = null;
    Image soundPink = null;
    Image drawnSound = null;
    Image back = null;
    Image backPink = null;
    Image drawnBack = null;
// placement for the images
    int musicx = 150;
    int musicy = 250;
    int soundx = 150;
    int soundy = 400;
    int backx = 550;
    int backy = 500;
// initalized the music volume as 1, the default
    static float musicVolume = 1;
    static float soundVolume = 1;
// initializes the sound and back button
    Sound cling1;
    boolean clicked = false;
    boolean backOver = false;

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
// load various media resouces for the class
        background = new Image("MainMenuBackground.png");
        music = new Image("music2.png");
        musicPink = new Image("musicPink2.png");
        drawnMusic = new Image("musicPink2.png");

        sound = new Image("sound2.png");
        soundPink = new Image("soundPink2.png");
        drawnSound = new Image("soundPink2.png");

        back = new Image("back.png");
        backPink = new Image("backPink.png");
        drawnBack = new Image("backPink.png");
        cling1 = new Sound("cling_1.wav");
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        boolean backSelected = false;
// this cuts a specific size of the pink label depending on where the mouse is clicked
        if ((mouseX >= musicx && mouseX <= musicx + music.getWidth()) && (mouseY >= musicy && mouseY <= musicy + music.getHeight()) && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            drawnMusic = musicPink.getSubImage(0, 0, mouseX - musicx, music.getHeight());
            musicVolume = (float) (mouseX - musicx) / music.getWidth();
        } else if ((mouseX >= soundx && mouseX <= soundx + sound.getWidth()) && (mouseY >= soundy && mouseY <= soundy + sound.getHeight()) && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            drawnSound = soundPink.getSubImage(0, 0,
                    mouseX - soundx, sound.getHeight());
            soundVolume = (float) (mouseX - soundx) / sound.getWidth();
            clicked = true;
        } else if ((mouseX >= backx && mouseX <= backx + back.getWidth()) && (mouseY >= backy && mouseY <= backy + back.getHeight())) {
            backSelected = true;
        }

        if (clicked && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            clicked = false;
            cling1.play();
        }
// this makes sure that the music and sound fill is always in line with the music and sound volume
        if (drawnMusic.getWidth() / music.getWidth() != musicVolume) {
            drawnMusic = musicPink.getSubImage(0, 0, (int) Math.floor(music.getWidth() * musicVolume), music.getHeight());
        }
        if (drawnSound.getWidth() / sound.getWidth() != soundVolume) {
            drawnSound = soundPink.getSubImage(0, 0, (int) Math.floor(sound.getWidth() * soundVolume), sound.getHeight());
        }
// enables the back button
        if (backSelected) {
            if (!backOver) {
                cling1.play();
                backOver = true;
            }
            drawnBack = backPink;
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                game.enterState(GameSkeleton.GAMEMAINMENU);
            }
        } else {
            drawnBack = back;
            backOver = false;
        }
// sets the global sound and music volumes
        container.setMusicVolume(musicVolume);
        container.setSoundVolume(soundVolume);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics graphics) throws SlickException {
// renders the background and buttons
        background.draw(0, 0);
        music.draw(musicx, musicy);
        sound.draw(soundx, soundy);
// draws the pink music and sounds over the old blue ones
        drawnMusic.draw(musicx, musicy);
        drawnSound.draw(soundx, soundy);
        drawnBack.draw(backx, backy);

    }
}
