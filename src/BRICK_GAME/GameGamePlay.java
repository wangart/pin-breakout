// run from GameSkeleton
package BRICK_GAME;

import java.awt.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.ShapeRenderer;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameGamePlay extends BasicGameState {

    int stateID = -1;

    GameGamePlay(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }
    Image rel = null;
    float pAngle;
    float btos;
    static int score;
    int drawnScore;
    int lives;
    Image brick1 = null;
    Image brick2 = null;
    Image brick3 = null;
    Image brick4 = null;
    Image brick5 = null;
    Image brick6 = null;
    Image wheel = null;
    Image arrow = null;
    Image paddle = null;
    Image bumper = null;
    Image bumperH = null;
    Image flipper = null;
    Image flipperH = null;
    Image ball = null;
    boolean close = false;
    Line spring;
    Rectangle rec[] = new Rectangle[5];
    Circle cir[] = new Circle[3];
    int trec[] = {1, 3, 5, 4, 6, 2};
    Line pad1;
    Line pad2;
    Rectangle r;
    Circle c;
    boolean rest = false;
    Line l[] = new Line[5];
    double lAngle[] = new double[l.length];
    int test = 0;
    Image plane = null;
    Image land = null;
    int newx = 100;
    int newy = 200;
    float x = 400;
    float y = 300;
    float scale = 1;
    float xspeed;
    float yspeed;
    float bspeed;
    float angle = 30;
    float angle2 = 150;
    float bAngle;
    boolean bounce;
    int bounceTime = 0;
    float padx1;
    float pady1;
    float padLength = 100;
    float lastX;
    float lastY;
    int lBounce;
    float speedM;
    float padx2;
    float pady2;
    float sprH = 650;
    boolean bHit[] = {false, false, false};
    boolean dead = false;
    float doorL = 150;
    float spinspeed;
    float spinS = 0;
    int arrowAng = 90;
    int spinAngle;
    int relNum = 0;
    Music BGM;
    static boolean reset = false;
    TrueTypeFont font;

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        //Initiating Varaiables
        rel = new Image("RELAUNCH.png");
        brick1 = new Image("block1.png");
        brick2 = new Image("block2.png");
        brick3 = new Image("block3.png");
        brick4 = new Image("block4.png");
        brick5 = new Image("block5.png");
        brick6 = new Image("block6.png");

        arrow = new Image("arrow.png");
        wheel = new Image("wheel.png");
        paddle = new Image("paddle.png");
        bumper = new Image("bumper.png");
        bumperH = new Image("bumperHit.png");
        flipper = new Image("flipper.png");
        ball = new Image("ball.png");
        flipperH = flipper.getFlippedCopy(true, false);
        BGM = new Music("gameplayMusic.wav");
    }

    void reset() throws SlickException {
        arrow = new Image("arrow.png");
        trec[0] = 3;
        trec[1] = 6;
        trec[2] = 2;
        trec[3] = 4;
        trec[4] = 1;
        trec[5] = 3;

        padx1 = (float) (400 + (padLength * Math.sin(angle)));
        pady1 = (float) (700 + (padLength * Math.cos(angle)));
        pad1 = new Line((float) 400, (float) 700, padx1, pady1);
        padx2 = (float) (400 + (padLength * Math.sin(angle2)));
        pady2 = (float) (70000 + (padLength * Math.cos(angle2)));
        pad2 = new Line((float) 400, (float) 700, padx1, pady1);
        spring = new Line(752, sprH, 800, sprH);
        l[0] = new Line(375, 150, 0, 0);
        l[1] = new Line(750, 700, 800, 700);
        l[2] = new Line(700, 0, 800, 100);
        l[3] = new Line(750, 700, 750, 500);
        l[4] = new Line(750, 500, 750, 450);
        lAngle[0] = 90;
        lAngle[1] = 0;
        lAngle[2] = 225;
        lAngle[3] = 90;
        lAngle[4] = -90;
        cir[0] = new Circle(200, 400, 30);
        cir[1] = new Circle(400, 350, 30);
        cir[2] = new Circle(600, 400, 30);
        c = new Circle(776, 120, 20);
        r = new Rectangle(400, 750, 100, 25);
        spring = new Line(752, sprH, 800, sprH);
        doorL = 150;
        padLength = 100;
        dead = false;
        bHit[0] = false;
        bHit[1] = false;
        bHit[2] = false;
        spinS = 0;
        angle = 30;
        angle2 = 150;
        bounceTime = 0;
        sprH = 650;
        score = 0;
        drawnScore = 0;
        lives = 2;
        lastX = c.getCenterX();
        lastY = c.getCenterY();
        spinAngle = 90;
        for (int cc = 0; cc < 5; cc += 1) {
            rec[cc] = new Rectangle(100 + cc * 100, 200, 100, 50);
        }
        close = false;
        c.setLocation(776, 570);
        xspeed = 0;
        yspeed = 0;
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        if (reset) {
            reset = false;
            reset();
        }
        if (!BGM.playing()) {
            BGM.loop();
        }
        //Initializes for input
        Input input = gc.getInput();

        //Controls the Arrow Graphic *Actual code for angle is in the line detection block*
        arrow.setCenterOfRotation(arrow.getWidth() / 4, 0);
        arrow.rotate(spinS);

        // Controls the dynamic lines that controls the pad openning
        l[0].set(375, 100, (int) (375 + 95 * Math.cos(Math.toRadians(lAngle[0]))), (int) (100 + 95 * Math.sin(Math.toRadians(lAngle[0]))));
        l[4].set(750, 500, (int) (750 + 95 * Math.cos(Math.toRadians(lAngle[4]))), (int) (500 + 95 * Math.sin(Math.toRadians(lAngle[4]))));
        l[2].set(700, 0, (int) (700 + doorL * Math.cos(Math.toRadians(45))), (int) (0 + doorL * Math.sin(Math.toRadians(45))));
        // Limits Y speed
        if (yspeed > 15) {
            yspeed = 15;
        }
        if (yspeed < -15) {
            yspeed = -15;
        }

        //Creates a small air friction affecting X speedzz
        if (yspeed > 0) {
            xspeed *= 0.99;
        }

        //Controls the flippers//////////////////////////////////////////////////////////
        padx1 = (float) (0 + (padLength * Math.cos(Math.toRadians(angle))));          //
        pady1 = (float) (700 + (padLength * Math.sin(Math.toRadians(angle))));         //
        pad1.set((float) 0, (float) 700, padx1, pady1);                               //
        padx2 = (float) (750 + (padLength * Math.cos(Math.toRadians(angle2))));        //
        pady2 = (float) (700 + (padLength * Math.sin(Math.toRadians(angle2))));        //
        pad2.set((float) 750, (float) 700, padx2, pady2);                              //
        //
             /*Accepts inputs to control flippers and changes the                      //
        speed modifier based on movement of the flipper */                      //
        //
        if (input.isKeyDown(Input.KEY_X) && angle2 < 210) {
            flipperH.setCenterOfRotation((float) (flipperH.getWidth() * 0.3 - 7), 7);
            flipperH.rotate(10);
            speedM = 10;                                                               //
            angle2 += 10;                                                              //
        } else if (!(input.isKeyDown(Input.KEY_X)) && angle2 > 150) {
            flipperH.setCenterOfRotation((float) (flipperH.getWidth() * 0.3 - 7), 7);
            flipperH.rotate(-10);
            speedM = -20;                                                              //
            angle2 -= 10;

        } else {
            speedM = (float) -30;                                                      //
        }                                                                              //
        //
        if (!(input.isKeyDown(Input.KEY_Z)) && angle < 30) {
            flipper.setCenterOfRotation(7, 7);
            flipper.rotate(10);
            speedM = (float) -20;                                                      //
            angle += 10;
        } else if (input.isKeyDown(Input.KEY_Z) && angle > -30) {
            flipper.setCenterOfRotation(7, 7);
            flipper.rotate(-10);
            speedM = 10;                                                               //
            angle -= 10;
        } else {                                                                       //
            speedM = -30;                                                              //
            speedM = (float) 0.7;                                                      //
        //
        }                                                                              //
        //
                /*Hit Detection and recalculation of                                   //
        ball angle and the x and y speeds.                                 //
        (lBounce prevents multiple bounces in a time frame */                 //
        //
        lBounce -= 1;                                                                  //
        //
        if (pad1.intersects(c) && bounce == true && lBounce <= 0) {                    //
            lBounce = 15;                                                              //
            if (xspeed > 0) {                                                          //
                bAngle = (float) Math.atan((yspeed / xspeed));                         //
            } else if (xspeed < 0) {                                                   //
                bAngle = (float) (Math.toRadians(180) + Math.atan((yspeed / xspeed))); //
            }                                                                          //
            bAngle = (float) (2 * Math.toRadians(angle) - bAngle);                     //
            bspeed = (float) Math.sqrt(Math.pow(yspeed, 2) + Math.pow(xspeed, 2));     //
            xspeed = (float) (Math.cos(bAngle) * (bspeed + speedM));                   //
            yspeed = (float) (Math.sin(bAngle) * (bspeed + speedM));                   //
        }                                                                              //
        //
        if (pad2.intersects(c) && bounce == true) {                    //
            lBounce = 15;                                                              //
            if (xspeed > 0) {                                                          //
                bAngle = (float) Math.atan((yspeed / xspeed));                         //
            } else if (xspeed < 0) {                                                   //
                bAngle = (float) (Math.toRadians(180) + Math.atan((yspeed / xspeed))); //
            }                                                                          //
            bAngle = (float) (2 * Math.toRadians(angle2) - bAngle);                    //
            bspeed = (float) Math.sqrt(Math.pow(yspeed, 2) + Math.pow(xspeed, 2));     //
            xspeed = (float) (Math.cos(bAngle) * (bspeed + speedM));                   //
            yspeed = (float) (Math.sin(bAngle) * (bspeed + speedM));                   //
        }                                                                              //
        /////////////////////////////////////////////////////////////////////////////////


        //Sets Limits on the ball////////////////////////////////////////////////////////
        if (c.getCenterX() - c.getRadius() <= 0) {                                     //
            c.setCenterX(c.radius + 1);                                                //
            bounce = true;                                                             //
            xspeed = (float) (-0.8 * xspeed);                                          //
        }                                                                              //
        if (c.getCenterX() + c.getRadius() >= 800) {                                   //
            c.setCenterX(799 - c.radius);                                              //
            bounce = true;                                                             //
            xspeed = (float) -0.8 * xspeed;                                            //
        }                                                                              //
        if (c.getCenterY() - c.getRadius() <= 0) {                                     //
            c.setCenterY(c.radius + 1);                                                //
            bounce = true;                                                             //
            yspeed = (float) (-0.8 * yspeed);                                          //
        }                                                                              //
        /////////////////////////////////////////////////////////////////////////////////

        //Controls the Paddle////////////////////////////////////////////////////////////
        //
        //
        if (input.getMouseX() > r.getCenterX() && r.getCenterX() < 550) {              //
            r.setCenterX((float) (r.getCenterX() + 1 + (input.getMouseX() - r.getCenterX()) * 0.1));
            if (c.getCenterY() < r.getMaxY() && c.getCenterY() > r.getMinY()) {                                                  //                                              //
                xspeed *= -0.9;                                                    //
            } else {                                                               //                                                 //
                r.setCenterX((float) (r.getCenterX() + 1 + (input.getMouseX() - r.getCenterX()) * 0.1));                                                   //
            }
        }                                                                              //
        //
        if (input.getMouseX() < r.getCenterX() && r.getCenterX() > 200) {              //
            r.setCenterX((float) (r.getCenterX() - 1 + (input.getMouseX() - r.getCenterX()) * 0.1));
        }                                                                              //
                  /*Paddle Hit Detection and recalculation                             //
        of X and Y speed */                                           //
        if (c.intersects(r) && bounce == true) {                                       //
            bounce = false;                                                            //
            yspeed *= -1.2;                                                            //
            xspeed += (float) ((c.getCenterX() - r.getCenterX()) * 0.2);
        }                                                                              //
        /////////////////////////////////////////////////////////////////////////////////

        //Universal Code preventing multiple bounces within a certain amount of frames //
        if (bounce == false) {                                                         //
            bounceTime += 1;                                                           //
            if (bounceTime >= 10) {                                                     //
                bounce = true;                                                         //
                bounceTime = 0;                                                        //
            }                                                                          //
        }                                                                              //
        /////////////////////////////////////////////////////////////////////////////////

        //  Controls the hit test for blocks ////////////////////////////////////////////
        for (int cc = 0; cc < 5; cc += 1) {                                            //
            if (rec[cc].intersects(c) && bounce == true && trec[cc] > 0) {            //
                if (c.getCenterY() < rec[cc].getMaxY() && c.getCenterY() > rec[cc].getMinY()) {
                    bounce = false;                                                    //
                    trec[cc] -= 1;                                                      //
                    xspeed *= -1.2;                                                    //
                } else {                                                               //
                    bounce = false;                                                    //
                    trec[cc] -= 1;                                                      //
                    yspeed *= -1.2;                                                    //
                }
                score += 100;
            }                                                                          //
        }                                                                              //
        /////////////////////////////////////////////////////////////////////////////////

        //Controls hit detection and physics of the ball against lines //////////////////
        for (int ccc = 0; ccc < l.length; ccc++) {                                     //
            if (c.intersects(l[ccc]) && bounce == true) {
                bspeed = (float) Math.sqrt(Math.pow(yspeed, 2) + Math.pow(xspeed, 2));
                if (ccc == 0) {
                    for (int cc = 0; cc < trec.length; cc++) {
                        if (trec[cc] <= 5) {
                            trec[cc] += 1;
                        }
                    }
                    if (xspeed >= 0) {
                        btos = (float) Math.toDegrees(Math.atan((100 - c.getCenterY()) / (375 - c.getCenterX())));
                    } else if (xspeed < 0) {                                               //
                        btos = (float) (180 + Math.toDegrees(Math.atan((100 - c.getCenterY()) / (375 - c.getCenterX()))));
                    }
                    if (btos < 0) {
                        btos += 360;
                    }
                    if (btos > lAngle[0]) {
                        spinS = -bspeed;
                    } else {
                        spinS = bspeed;
                    }
                }

                if (ccc == 3) {
                    c.setCenterX(749 - c.radius);
                }//
                if (xspeed >= 0) {                                                     //
                    if (xspeed == 0) {                                                 //
                        bAngle = (float) Math.toRadians(-90);                          //
                    } else {                                                           //
                        bAngle = (float) Math.atan((yspeed / xspeed));                 //
                    }                                                                  //
                } else if (xspeed < 0) {                                               //
                    bAngle = (float) (Math.toRadians(180) + Math.atan((yspeed / xspeed)));
                }
                bAngle = (float) (2 * Math.toRadians(lAngle[ccc]) - bAngle);           //
                //
                xspeed = (float) (Math.cos(bAngle) * bspeed);                          //
                yspeed = (float) (Math.sin(bAngle) * bspeed);                          //
                bounce = false;                                                        //
            }

        //
        }                                                                              //
        /////////////////////////////////////////////////////////////////////////////////
        //Controls the launcher spring //////////////////////////////////////////////////
        //
             /*Hit Detection for spring and ball*/                                     //
        //
        if (spring.intersects(c)) {                                                    //
            yspeed = 0;                                                                //
            xspeed = 0;                                                                //
            yspeed -= 0.2;                                                             //
            if (rest == false) {                                                       //
                c.setCenterY(spring.getMaxY() - c.getRadius());                        //
                rest = true;                                                           //
            }                                                                          //
        }                                                                              //
              /* Lowers the Spring */                                                  //
        if (input.isKeyDown(Input.KEY_SPACE) && sprH <= 675) {                         //
            sprH += 0.5;                                                               //
        }                                                                              //
              /* Launches ball */                                                      //
        if ((!input.isKeyDown(Input.KEY_SPACE)) && sprH > 650) {                       //
            sprH -= (sprH - 650) / 2;                                                  //
            if (spring.intersects(c)) {                                                //
                yspeed = -(sprH - 635);                                                //
                close = true;                                                          //
            }                                                                          //
        }                                                                              //
              /* Closes door*/                                                         //
        if (close == true && lAngle[4] < -45) {                                        //
            lAngle[4] += 0.2;                                                          //
        }
        if (close == false && lAngle[4] > -90) {
            lAngle[4] -= 0.2;
        }//
        spring.setCenterY(sprH);                                                       //
        //
        /////////////////////////////////////////////////////////////////////////////////

        //Controls the bumpers //////////////////////////////////////////////////////////
        for (int cc = 0; cc < cir.length; cc += 1) {
            if (cir[cc].intersects(c)) {
                score += 100;
                bHit[cc] = true;
                float tang = -1 / ((cir[cc].getCenterY() - c.getCenterY()) / (cir[cc].getCenterX() - c.getCenterX()));
                if (xspeed >= 0) {                                                     //
                    if (xspeed == 0) {                                                 //
                        bAngle = (float) Math.toRadians(-90);                          //
                    } else {                                                           //
                        bAngle = (float) Math.atan((yspeed / xspeed));                 //
                    }
                } else if (xspeed < 0) {                                               //
                    bAngle = (float) (Math.toRadians(180) + Math.atan((yspeed / xspeed)));
                }
                bAngle = (float) (2 * Math.atan(tang) - bAngle);                         //
                bspeed = (float) Math.sqrt(Math.pow(yspeed, 2) + Math.pow(xspeed, 2)); //
                xspeed = (float) (Math.cos(bAngle) * bspeed * 1.4);                    //
                yspeed = (float) (Math.sin(bAngle) * bspeed * 1.4);
            } else {
                bHit[cc] = false;
            }
        }
        //Gravity and Updating of ball location
        yspeed += 0.2;
        c.setCenterX(c.getCenterX() + xspeed);
        c.setCenterY(c.getCenterY() + yspeed);
        ball.setCenterOfRotation(c.getCenterX() - c.getMinX(), c.getCenterY() - c.getMinY());
        ball.rotate(100);

        //Controls Death and new Ball////////////////////

        /*Opens Gap for new ball*/
        if (doorL < 150 && dead == false) {
            doorL += 0.5;
        }
        /*Opens door and resets Gap*/
        if (dead == true) {
            if (doorL > 50) {
                doorL -= 0.5;
            }
            if (doorL < 51) {
                dead = false;
                lives -= 1;
                c.setCenterX(775);
                c.setCenterY(30);
                xspeed = 0;
                yspeed = 0;
            }
        }
        if (c.getCenterY() > 1000 && dead == false) {
            if (lives > 0) {

                dead = true;
                close = false;
            }
        }


        //Spinner Code/////////////////////////////////////////////////////////////////
        spinS = (float) (spinS * 0.975);
        lAngle[0] += spinS;
        if (lAngle[0] >= 360) {
            lAngle[0] = lAngle[0] % 360;
        }
        if (lAngle[0] < 0) {
            lAngle[0] += 360;
        }
        relNum -= 1;
        /* gives rewards based on outcome*/
        if (Math.abs(spinS) < 0.05 && spinS != 0) {
            spinS = 0;
            if (lAngle[0] > 270 && lAngle[0] < 290) {
                score += 250;
            } else if (lAngle[0] > 290 && lAngle[0] < 310) {
                lives += 1;
            } else if (lAngle[0] > 310 && lAngle[0] < 330) {
                score += 250;
            } else if (lAngle[0] > 330 || lAngle[0] < 30) {
                score += 750;
            } else if (lAngle[0] > 30 && lAngle[0] < 90) {
                score += 1000;
            } else if (lAngle[0] > 90 && lAngle[0] < 110) {
                score += 50;
            } else if (lAngle[0] > 110 && lAngle[0] < 130) {
                score += 1000;
            } else if (lAngle[0] > 130 && lAngle[0] < 150) {
                score += 500;
            } else if (lAngle[0] > 150 && lAngle[0] < 210) {
                close = false;
                c.setLocation(776, 570);
                xspeed = 0;
                yspeed = 0;
                relNum = 100;
            } else if (lAngle[0] > 210 && lAngle[0] < 270) {
                score += 500;
            }
        }
        if (drawnScore < score) {
            drawnScore += 5;
        }

        if (c.getMinY() > 800 && lives == 0) {
            GameEndGame.reset = true;
            game.enterState(GameSkeleton.GAMEENDGAME);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //////////////DRAWS ALL THE OBJECTS BASED ON CERTAIN VARIABLES/////////////////
    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        wheel.draw(310, 34, 0.3f);
        for (int c = 0; c < l.length; c++) {
            ShapeRenderer.draw(l[c]);
        }
        ShapeRenderer.draw(spring);
        ShapeRenderer.draw(r);

        for (int count = 0; count < 5; count += 1) {
            if (trec[count] > 0) {
                ShapeRenderer.draw(rec[count]);
            }
            System.out.println(trec[count]);
            if (trec[count] == 1) {
                brick1.draw(rec[count].getMinX() - 4, rec[count].getMinY() - 4, 1.5f);
            } else if (trec[count] == 2) {
                brick2.draw(rec[count].getMinX() - 4, rec[count].getMinY() - 4, 1.5f);
            } else if (trec[count] == 3) {
                brick3.draw(rec[count].getMinX() - 4, rec[count].getMinY() - 4, 1.5f);
            } else if (trec[count] == 4) {
                brick4.draw(rec[count].getMinX() - 4, rec[count].getMinY() - 4, 1.5f);
            } else if (trec[count] == 5) {
                brick5.draw(rec[count].getMinX() - 4, rec[count].getMinY() - 4, 1.5f);
            } else if (trec[count] == 6) {
                brick6.draw(rec[count].getMinX() - 4, rec[count].getMinY() - 4, 1.5f);
            }
        }

        for (int count = 0; count < cir.length; count += 1) {

            if (bHit[count] == false) {
                bumper.draw(cir[count].getMinX(), cir[count].getMinY(), 0.27f);
            } else {
                bumperH.draw(cir[count].getMinX(), cir[count].getMinY(), 0.27f);
            }
        }

        paddle.draw(r.getMinX() - 5, r.getMinY() - 5, 0.39f);
        flipper.draw(-10, 690, 0.3f);
        flipperH.draw(655, 690, 0.3f);
        ball.drawCentered(775, 30);
        ball.draw(c.getMinX(), c.getMinY(), 1.15f);
        g.drawString(String.valueOf(lives), 772, 27);
        arrow.draw(375 - arrow.getWidth() / 4, 100, 0.5f);
        if (relNum > 0) {
            rel.drawCentered(400, 400);
        }
        char[] temp = Integer.toString(drawnScore).toCharArray();
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
        g.drawString(print + "" + finalNum, 585, 50);
    }
}