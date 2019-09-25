package com.charlie.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // Android debug variables
    final static String TAG="TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;



    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
int playerX;
int playerY;
int ememyXPosition;
int enemyYPosition;

    String fingerAction = "mouseup";

    // ----------------------------
    // ## GAME STATS
    // ----------------------------
    int score = 0;
    int lives = 5;

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites


        this.playerX = 100;
        this.playerY = 120;

        this.ememyXPosition = 500;
        this.enemyYPosition =  120;
        // @TODO: Any other game setup

    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        // put player in middle of screen --> you may have to adjust the Y position
        // depending on your device / emulator


    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    public void updatePositions() {
        // @TODO: Update position of player


        if(this.fingerAction == "mousedown"){

            this.playerY = this.playerY - 10;

        }
        if(this.fingerAction == "mouseup"){
            this.playerY = this.playerY + 10;
        }



    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);


            //@TODO: Draw the player

            Bitmap playerImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                    R.drawable.player_ship);

            canvas.drawBitmap(playerImage, playerX, playerY, paintbrush);

            //@TODO: Draw the enemy

            Bitmap enemyImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                    R.drawable.alien_ship2);

            canvas.drawBitmap(enemyImage, ememyXPosition, enemyYPosition, paintbrush);


            // Show the hitboxes on player and enemy


            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // move player up
            fingerAction = "mouse down";

            Log.d(TAG,"Player tapped the screen");
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // move player down
            fingerAction = "Player lifted the finger";
            Log.d(TAG,"Person lifted finger");
        }

        return true;
    }
}
